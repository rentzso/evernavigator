import unittest
import requests
import json


baseurl = 'http://ec2-79-125-43-66.eu-west-1.compute.amazonaws.com:8080/twitter-api-1.0.0/twitter/'
##baseurl = 'http://localhost:8080/twitter-api-1.0.0/twitter/'


tweets_req = 'tweets'
follow_req = 'follow'
unfollow_req = 'unfollow'
followers_req = 'followers'
headers = {'content-type': 'application/json'}


def testResult(url, values):
	return requests.post(url, data=json.dumps(values) , headers=headers)

def getTextListOfTweets(tweets):
	out = []
	for tweet in tweets:
		out.append(tweet['text'])
	return sorted(out)


class TestTwitterAPI(unittest.TestCase):


	def setUp(self):
		## dictionary with users and apikey
		self.twitter_users = {'sconosciuto':'echiloconoscelui', 'papadirenzo':'eilpapadirenzono',
		                      'rentzso':'belloeimpossibil', 'juliavc': 'bellatroppobella'}
		self.tweets = {'rentzso': 'julia I love you :)', 
		               'juliavc': 'renzo I love you :)', 
                       'sconosciuto': 'renzo, julia who are they?',
                       'papadirenzo': 'I am renzo\'s father'}


	def test_1_follow(self):
		result = self.follow('rentzso', 'juliavc')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json()['success'], 'true')

		result = self.follow('juliavc','rentzso')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json()['success'], 'true')

		result = self.follow('papadirenzo','rentzso')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json()['success'], 'true')

		result = self.getTweets('rentzso')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(getTextListOfTweets(result.json()),
			sorted([self.tweets['juliavc'], self.tweets['rentzso'] ]) )

		result = self.getTweets('papadirenzo')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(getTextListOfTweets(result.json()),
			sorted([self.tweets['papadirenzo'], self.tweets['rentzso'] ]) )

		result = self.getTweets('sconosciuto')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(getTextListOfTweets(result.json()),
			[self.tweets['sconosciuto']])

		result = self.getFollowers('sconosciuto')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json(),{'followers':[],'followed':[]})

		result = self.getFollowers('rentzso')
		self.assertEqual(result.status_code, 200)
		r = result.json()
		followers = sorted(r['followers'])
		self.assertEqual(followers, ['juliavc', 'papadirenzo'])
		followed = r['followed']
		self.assertEqual(followed, ['juliavc'])

	def test_2_unfollow(self):
		result = self.unfollow('rentzso', 'juliavc')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json()['success'], 'true')

		result = self.unfollow('juliavc','rentzso')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json()['success'], 'true')

		result = self.unfollow('papadirenzo','rentzso')
		self.assertEqual(result.status_code, 200)
		self.assertEqual(result.json()['success'], 'true')

		for key in self.twitter_users.keys():
			result = self.getTweets(key)
			self.assertEqual(result.status_code, 200)
			self.assertEqual(getTextListOfTweets(result.json()),
				[self.tweets[key]])

			result = self.getFollowers(key)
			self.assertEqual(result.status_code, 200)
			self.assertEqual(result.json(), {'followers':[],'followed':[]})

	def test_3_unauthorized(self):
		keep_users = {} 
		for key in self.twitter_users.keys():
			keep_users[ key ] = self.twitter_users[ key ]
			self.twitter_users[key] = 'wrongapikey'

		result = self.follow('rentzso', 'juliavc')
		self.assertEqual(result.status_code, 401)
		self.assertEqual(result.json()['success'], 'false')

		result = self.follow('juliavc','rentzso')
		self.assertEqual(result.status_code, 401)
		self.assertEqual(result.json()['success'], 'false')

		result = self.follow('papadirenzo','rentzso')
		self.assertEqual(result.status_code, 401)
		self.assertEqual(result.json()['success'], 'false')

		for key in self.twitter_users.keys():
			result = self.getTweets( key )
			self.assertEqual(result.status_code, 401)
			self.assertEqual(result.json(), [])

			result = self.getFollowers( key )
			self.assertEqual(result.status_code, 401)
			self.assertEqual(result.json()['success'], 'false')

		## restoring users
		self.twitter_users = keep_users

	def test_4_no_unauthorized_actions(self):

		for key in self.twitter_users.keys():
			result = self.getTweets( key )
			self.assertEqual(result.status_code, 200)
			self.assertEqual(getTextListOfTweets(result.json()),
				[self.tweets[ key ]])

			result = self.getFollowers( key )
			self.assertEqual(result.status_code, 200)
			self.assertEqual(result.json(),{'followers':[],'followed':[]})


	## helper functions for testing
	def follow(self, username, followed):
		url = baseurl + follow_req
		values = {}
		values['followed'] = followed
		values['username'] = username
		values['apiKey'] = self.twitter_users[username]
		return testResult(url, values)

	def unfollow(self, username, followed):
		url = baseurl + unfollow_req
		values = {}
		values['followed'] = followed
		values['username'] = username
		values['apiKey'] = self.twitter_users[username]
		return testResult(url, values)

	def getTweets(self, username):
		url = baseurl + tweets_req
		values = {}
		values['username'] = username
		values['apiKey'] = self.twitter_users[username]
		return testResult(url, values)

	def getFollowers(self, username):
		url = baseurl + followers_req
		values = {}
		values['username'] = username
		values['apiKey'] = self.twitter_users[username]
		return testResult(url, values)


suite = unittest.TestLoader().loadTestsFromTestCase(TestTwitterAPI)
unittest.TextTestRunner(verbosity=2).run(suite)
