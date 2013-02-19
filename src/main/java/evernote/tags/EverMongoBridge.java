package evernote.tags;

import java.net.UnknownHostException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import test.Main;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.thrift.TException;


import com.classes.data.*;

public class EverMongoBridge {
	private EvernoteHandle handle;
	private MongoOperations mongoOps;
	private HashMap<String, List<Note>> tagGuidToNotes = new HashMap<String, List<Note>>();
	private int userId;
	private static final Log log = LogFactory.getLog(EverMongoBridge.class);
	/**
	 * EvernoteHandle handle: class to interact with evernote data
	 * int userId: the unique identifier of the user
	 * @throws UnknownHostException 
	 */
	
	@Autowired
	public EverMongoBridge(MongoTemplate mongoOps, EvernoteHandle handle, int userId) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException, UnknownHostException{
		this.mongoOps = mongoOps;
		this.handle = handle;
		this.userId = userId;
		
	}

	/**
	 * dumpTagsToCollection
	 * ---------------------
	 * method to Store tags in mongoDB
	 * following this Mongo Schema
	 * 
	 * {
     *   userId: Number,
	 *   tag: String, 
	 *   guid: String, //tag unique identifier
	 *   count: Number, // number of notes for which the tag has been used.
	 *   notes: [{guid: String, title: String}], // list of guids and titles of the notes tagged with this tag
	 *   related: [{tag: String, guid: String, count: Number}] // list of related tags
	 * }
	 *
	 * just the notes are missing ( use the method "dumpNotesToMongo" to store them )
	 */
	public void dumpTagsToCollection() throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
		HashMap<String, Integer> tagCount;
		int count, countSubTag;
		ArrayList<RelatedTag> relatedTags;
		
		HashMap<String, HashMap<String, Integer>> allTagsCounts = this.handle.getAllTagsCounts();
		for (String guid: allTagsCounts.keySet() ){
			tagCount = allTagsCounts.get(guid); 
			if (tagCount.size()!=0){
				String tagName = this.handle.idToTag.get(guid);
				log.info(tagName);
				relatedTags = new ArrayList<RelatedTag>();
				count = 0;
				for (String guidSubTag: tagCount.keySet()){
					countSubTag = tagCount.get(guidSubTag);
					
					if (guid.equals(guidSubTag)){
						count = countSubTag;
					} else {
						relatedTags.add(new RelatedTag(handle.idToTag.get(guidSubTag), guidSubTag, countSubTag ));
					}
				}
				this.mongoOps.insert(new Tag(this.userId, tagName, guid, count, null, relatedTags));
			}
			
			
		}
	}

	/**
	 * dumpNotesToMongo
	 * --------------------
	 * This method stores for each tag all the notes
	 * that references it
	 */
	public void dumpNotesToMongo() throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
	
		List<NoteMetadata> notes = this.handle.getAllNotes();
		
		List<String> tags;
		String guid;
		String title;
		this.tagGuidToNotes.clear();
		for (NoteMetadata note: notes){
			tags = note.getTagGuids();
			guid = note.getGuid();
			title = note.getTitle();
			
			if (tags != null){
				for (String guidTag: tags){
					List<Note> tagNotes = tagGuidToNotes.get(guidTag);
					if (tagNotes == null){
						tagNotes = new ArrayList<Note>();
						tagNotes.add( new Note(title, guid) );
						this.tagGuidToNotes.put(guidTag, tagNotes);
					} else {
						tagNotes.add( new Note(title, guid) );
					}	
				}
			}
		}
		
		for (String tagGuid: this.tagGuidToNotes.keySet()){
			List<Note> tagNotes = this.tagGuidToNotes.get(tagGuid);
			if (tagNotes != null){
				this.mongoOps.updateFirst(new Query(where("guid").is(tagGuid)), update("notes",tagNotes), Tag.class);
			}
		}
	}

}

