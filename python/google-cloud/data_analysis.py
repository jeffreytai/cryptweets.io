import json

GOOGLE_CLOUD_SUPPORTED_LANGUAGES = ['zh', 'zh-Hant', 'en', 'fr', 'de', 'it', 'ja', 'ko', 'pt', 'es']

"""Verify authentication to Google Cloud API"""
def authenticate():
    from google.cloud import storage

    # If you don't specify credentials when constructing the client, the
    # client library will look for credentials in the environment.
    storage_client = storage.Client()

    # Make an authenticated API request
    buckets = list(storage_client.list_buckets())
    print(buckets)

    return len(buckets) > 0

def collect_results():
    tweets_data_path = 'data/twitter_data.txt'

    tweets_data = []
    tweets_file = open(tweets_data_path, "r")
    for line in tweets_file:
        try:
            tweet = json.loads(line)
            tweets_data.append(tweet)
        except:
            continue

    print('result count: {}'.format(len(tweets_data)))
    return tweets_data

def analyze_sentiment(data):
    # Import Google Cloud client library
    from google.cloud import language
    from google.cloud.language import enums
    from google.cloud.language import types

    # Instantiate client
    client = language.LanguageServiceClient()

    # Text to analyze
    for tweet in data:
        if tweet['lang'] in GOOGLE_CLOUD_SUPPORTED_LANGUAGES:
            document = types.Document(
                content=tweet['text'],
                type=enums.Document.Type.PLAIN_TEXT
            )

            # Detects the sentiment of the text
            sentiment = client.analyze_sentiment(document=document).document_sentiment

            print('Text: {}'.format(tweet['text']))
            print('Sentiment: {}, {}'.format(sentiment.score, sentiment.magnitude))


if authenticate():
    tweets_data = collect_results()
    analyze_sentiment(data=tweets_data)
