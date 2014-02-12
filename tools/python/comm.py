import http.client, urllib.parse
import hashlib

class Communicator(object):
    __rest = None
    __login = ""
    __password = ""
    __gettoken_params = None
    __headers = {"Content-type": "application/x-www-form-urlencoded"}

    def __init__(self, email, password):
        self.__login = email
        md5 = hashlib.md5()
        md5.update(password.encode())
        self.__password = md5.hexdigest()
        self.__gettoken_params = urllib.parse.urlencode({'email': email})
    
    def __del__(self):
        if self.__rest != None:
            self.__rest.close()
    
    def Start(self):
        try:
            self.__rest = http.client.HTTPConnection('api.ivona.com', 80) 
        except http.client.HTTPException as ex: 
            print(ex, "\n")
    
    def CallMethod(self, method_url, params={}, method="POST"):
        #get token     
        self.__rest.request("POST", "/api/saas/rest/tokens/", self.__gettoken_params, self.__headers)
        token = eval(self.__rest.getresponse().read().decode())
    
        #compute md5
        md5 = hashlib.md5()
        md5.update(self.__password.encode())
        md5.update(token.encode())    
        tokenized_pass = md5.hexdigest()
    
        params["token"] = token
        params["md5"] = tokenized_pass

        #call mathod
        if method == "GET" or method == "PUT" or method == "DELETE":
            method_url += "?" + urllib.parse.urlencode(params)
            self.__rest.request(method, method_url)
        else:
            self.__rest.request(method, method_url, urllib.parse.urlencode(params), self.__headers)

        return self.__rest.getresponse().read().decode()

