package evernote.tags;

import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.evernote.thrift.*;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;
import com.evernote.edam.type.*;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.*;

public class EvernoteHandle {
	/**
	 * List of utility to get data from evernote
	 */
	private NoteStore.Client noteStore;
	private String accessToken;
	HashMap<String, String> tagToID = new HashMap<String, String>();
	HashMap<String, String> idToTag = new HashMap<String, String>();
	
	private static final Log log = LogFactory.getLog(EvernoteHandle.class);
	
	public EvernoteHandle(String accessToken, String noteStoreUrl) throws EDAMUserException, EDAMSystemException, TException{
		// initialize the handle
		// storing the accessToken
		// and the noteStore used 
		// to interact with Evernote
		THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
		this.noteStore = new NoteStore.Client(noteStoreProt, noteStoreProt);
		this.accessToken = accessToken;
		this.initializeTags();
	}
	
	public List<?> getNotebooks() throws EDAMUserException, EDAMSystemException, TException{
		List<?> notebooks = this.noteStore.listNotebooks(this.accessToken);
		return notebooks;
	}
	
	public List<?> getTags() throws EDAMUserException, EDAMSystemException, TException{
		List<?> tags = this.noteStore.listTags(this.accessToken);
		return tags;
	}
	
	private void initializeTags() throws EDAMUserException, EDAMSystemException, TException{
		/**
		 * utility that initialize the two maps
		 * that convert from guid (unique identifier) to tags
		 * and vice versa
		 */
		int count = 0;
		for (Object tag: this.getTags() ){
			count++;
			String name = ((Tag)tag).getName();
			String guid = ((Tag)tag).getGuid();
			this.tagToID.put(name, guid);
			this.idToTag.put(guid, name);
		}
		log.info("TagCount: " + count);
	}
	
	public Map<String, Integer> getTagsCount(String guid) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
		/**
		 * from an evernote guid
		 * return the number of related notes
		 */
		NoteFilter filter = new NoteFilter();
		filter.addToTagGuids(guid);
		return this.noteStore.findNoteCounts(accessToken, filter, false).getTagCounts();
	}

	public HashMap<String, HashMap<String, Integer>> getAllTagsCounts() throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
		List<NoteMetadata> notes = this.getAllNotes();
		HashMap<String, HashMap<String, Integer>> results = new HashMap<String, HashMap<String, Integer>>();
		for (String guid: this.idToTag.keySet()){
			results.put(guid, new HashMap<String, Integer>());
		}
		
		for (NoteMetadata note: notes ){
			if (note.getTagGuids() != null){
				for (String guid: note.getTagGuids()){
					HashMap<String, Integer> map = results.get(guid);
					for (String guid1: note.getTagGuids()){
						if (map.containsKey(guid1)){
							map.put(guid1, map.get(guid1) + 1);
						} else {
							map.put(guid1, 1);
						}
					}
				}
			}
		}
		return results;
	}
	
	public List<NoteMetadata> getAllNotes() throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
		NoteFilter filter = new NoteFilter();
		NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
		spec.setIncludeTagGuids(true);
		spec.setIncludeTitle(true);
		return this.noteStore.findNotesMetadata(accessToken, filter, 0, 400, spec).getNotes();
	}
	
}