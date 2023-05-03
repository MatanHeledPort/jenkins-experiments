@Grab(group='com.squareup.okhttp3', module='okhttp', version='4.9.3')
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
def CLIENT_ID = 'Cj2AxmOgMNzpCYvZB7OHPGNZ3qA4EW9G'
def CLIENT_SECRET = 'fCF2gW9gbAP4OEsoKVXU1s28kqaw2hb2AGpfIwZaoabT8cCAlmHZEtPEKm8qXIyF'
def API_URL = 'https://api.getport.io/v1'
def credentials = [clientId: CLIENT_ID, clientSecret: CLIENT_SECRET]
def jsonMediaType = MediaType.parse("application/json; charset=utf-8")
def client = new OkHttpClient()
def tokenRequestBody = RequestBody.create(jsonMediaType, credentials as String)
def tokenRequest = new Request.Builder()
		.url("${API_URL}/auth/access_token")
		.post(tokenRequestBody)
		.build()
def tokenResponse = client.newCall(tokenRequest).execute()
def access_token = new groovy.json.JsonSlurper().parseText(tokenResponse.body().string()).accessToken
entity = [
	identifier: "some_identifier",
	title: "Some Title",
	properties: [
		version: "string",
		environment: "string",
		status: "string",
		duration: "string",
		// job-url: "[http://35.211.121.207:8080/job/port-pipline]",
	],
	relations: [
		microservice: "THE IDENTIFIER OF THE RELATED DEPLOYMENT",
	]
]
def entityRequestBody = RequestBody.create(jsonMediaType, new groovy.json.JsonBuilder(entity).toString())
def entityRequest = new Request.Builder()
		.url("${API_URL}/blueprints/deployment/entities?upsert=true")
		.post(entityRequestBody)
		.addHeader("Authorization", "Bearer ${access_token}")
		.build()
def entityResponse = client.newCall(entityRequest).execute()
println(entityResponse.body().string())
