from flask import Flask, request, jsonify
import joblib
import pandas as pd
from datetime import datetime

# Create an instance of the Flask application
app = Flask(__name__)

# --- Load the Trained Model ---
# We load the model once when the application starts.
model_filename = 'volatility_model.joblib'
print(f"Loading model from {model_filename}...")
model = joblib.load(model_filename)
print("Model loaded successfully.")


# This is the "Hello World" endpoint from before, good for checking if the server is up.
@app.route('/')
def hello_world():
    return 'Hello from the Volatility Service AI Brain! 🧠'


# --- Create the Prediction Endpoint ---
@app.route('/predict', methods=['POST'])
def predict():
    # Get the JSON data sent from the client (our Java app)
    input_data = request.get_json()

    # --- Feature Engineering (must match the training script) ---
    # We need to create the same features we used to train the model.
    try:
        # We need strike_price, expirationDate, and type from the input
        strike_price = float(input_data['strike_price'])
        expiration_date_str = input_data['expirationDate'] # e.g., "2025-12-31"
        option_type = input_data['type'] # "call" or "put"

        # Calculate time_to_expiry
        expiration_date = datetime.strptime(expiration_date_str, '%Y-%m-%d')
        time_to_expiry = (expiration_date - datetime.now()).days / 365.25

        # Create moneyness (using the same assumed stock price from training)
        ASSUMED_STOCK_PRICE = 170.0 
        moneyness = ASSUMED_STOCK_PRICE / strike_price

        # Create type_numeric
        type_numeric = 1 if option_type.lower() == 'call' else 0

        # Create a DataFrame for the model, ensuring the column order is the same
        features_df = pd.DataFrame([[time_to_expiry, strike_price, moneyness, type_numeric]],
                                   columns=['time_to_expiry', 'strike_price', 'moneyness', 'type_numeric'])

        # --- Make a Prediction ---
        prediction = model.predict(features_df)
        predicted_volatility = prediction[0] # The result is a numpy array

        # --- Return the Result as JSON ---
        return jsonify({'predicted_volatility': predicted_volatility})

    except Exception as e:
        # If any error occurs, return an error message
        return jsonify({'error': str(e)}), 400


if __name__ == '__main__':
    # Starts the development server on port 5000
    app.run(debug=True, port=5000)