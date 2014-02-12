import json
import urllib.request
from urllib.error import HTTPError
from comm import Communicator
import sys
import getopt

class Client(object):
    __web_comm = None

    def __init__(self, email, password):
        self.__web_comm = Communicator(email, password)

    def Start(self):
        self.__web_comm.Start()

    def CreateSpeechFile(self, text, ctype, voice_id, codec_id, additional={}):
        params={"text": text,
                "contentType": ctype,
                "voiceId": voice_id,
                "codecId": codec_id,
                "params": additional}
        content = self.__web_comm.CallMethod("/api/saas/rest/speechfiles/", params)
        content = json.loads( content )
        return content["soundUrl"]


if __name__ == "__main__":

    text = "It's hard to overstate my satisfaction"
    out = "out.mp3"
    voice = "pl_jan"

    optlist, args = getopt.getopt(sys.argv[1:], "t:o:", ["text=", "out=", "voice="])
    for opt, val in optlist:
        if opt in ("-t", "--text"):
            text = val
        elif opt in ("-v", "--voice"):
            voice = val
        elif opt in ("-o", "--out"):
            out = val

    c = Client("IVONA_SERVICE_USER_EMAIL", "IVONA_SERVICE_USER_SECRET_TOKEN")
    c.Start()
    
    #get URL to the generated sound file
    soundUrl = c.CreateSpeechFile(text, "text/plain", voice, "mp3/22050")

    retry = 0
    while retry < 3:
        try:
            response = urllib.request.urlopen(soundUrl)
            mp3 = response.read()
            open(out, "wb").write( mp3 )
            break
        except HTTPError as e:
            retry += 1


