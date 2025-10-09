#importing Flask class
from flask import Flask

#Creating an application instance
app = Flask(__name__)

#Listen requests on root URL("/") to call the below function
@app.route('/')
def hello_world():
	return 'Hello from the Brain behind Volatility Service'

#Ensuring this Flask Server only runs on direct execution of Script and not on import by an external file
if __name__ == '__main__':
	app.run(debug=True) 
