import requests
import json

baseurl = 'http://ec2-79-125-43-66.eu-west-1.compute.amazonaws.com:8080/twitter-api-1.0.0/twitter/'

tweets_req = 'tweets'
follow_req = 'follow'
unfollow_req = 'unfollow'
followers_req = 'followers'


def testReq(url, values):
	headers = {'content-type': 'application/json'}
	result = requests.post(url, data=json.dumps(values) , headers=headers)
	print result
	print result.text
	print
	print raw_input('Press enter to continue')


def testFollowers():
	url = baseurl + followers_req
	print url

	values = {}
	values['apiKey'] = 'echiloconoscelui'
	values['username'] = 'sconosciuto'
	print 'followers and followed ones by', values['username']
	testReq(url, values)
	values['apiKey'] = 'eilpapadirenzono'
	values['username'] = 'papadirenzo'
	print 'followers and followed ones by', values['username']
	testReq(url, values)
	values['apiKey'] = 'belloeimpossibil'
	values['username'] = 'rentzso'
	print 'followers and followed ones by', values['username']
	testReq(url, values)
	values['apiKey'] = 'eilpapadirenzono'
	print 'wrong api key for', values['username']
	testReq(url, values)

def testTweets():
	url = baseurl + tweets_req
	print url
	values = {}
	values['apiKey'] = 'echiloconoscelui'
	values['username'] = 'sconosciuto'
	print 'tweets seen by', values['username']
	testReq(url, values)
	values['apiKey'] = 'eilpapadirenzono'
	values['username'] = 'papadirenzo'
	print 'tweets seen by', values['username']
	testReq(url, values)
	values['apiKey'] = 'belloeimpossibil'
	values['username'] = 'rentzso'
	print 'tweets seen by', values['username']
	testReq(url, values)
	values['keyword'] = 'julia'
	print 'tweets seen by', values['username']
	print 'with keyword:', values['keyword']
	testReq(url, values)
	values['apiKey'] = 'eilpapadirenzono'
	print 'wrong api key for', values['username']
	testReq(url, values)


def testFollow():
	url = baseurl + follow_req
	print url
	values = {}
	values['apiKey'] = 'eilpapadirenzono'
	values['username'] = 'rentzso'
	values['followed'] = 'papadirenzo'
	print 'wrong api key for', values['username']
	testReq(url, values)
	values['apiKey'] = 'belloeimpossibil'
	print 'follower and followed ones by', values['username']
	testReq(baseurl + followers_req, values)
	print values['username'], 'followed', values['followed']
	testReq(url, values)
	print 'follower and followed ones by', values['username']
	testReq(baseurl + followers_req, values)
	

def testUnfollow():
	url = baseurl + unfollow_req
	print url
	values = {}
	values['apiKey'] = 'eilpapadirenzono'
	values['username'] = 'rentzso'
	values['followed'] = 'papadirenzo'
	print 'wrong api key for', values['username']
	testReq(url, values)
	values['apiKey'] = 'belloeimpossibil'
	print values['username'], 'unfollowed', values['followed']
	testReq(url, values)
	print 'follower and followed ones by', values['username']
	testReq(baseurl + followers_req, values)
	


testFollowers()
testTweets()
testFollow()
testUnfollow()



