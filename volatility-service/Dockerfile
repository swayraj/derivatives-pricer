#Using a lightweight python image as base
FROM python:3.11-slim

#Setting the working directory inside container to /app
WORKDIR /app

#Copy the requirements file into the container at /app
COPY requirements.txt .

#Install the Python Dependencies from the requirements file
RUN pip install --no-cache-dir -r requirements.txt

# Copying application's code from local machine
#to the container at /app
COPY . .

#Listen requests on port
EXPOSE 5000

#Final command to run on start of container
CMD ["python", "app.py"]



