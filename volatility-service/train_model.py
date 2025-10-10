import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
import joblib
import numpy as np
from datetime import datetime

# --- 1. Load the Data ---
data = pd.read_csv('real_options_data.csv')

# --- 2. Feature Engineering ---
# The model needs numerical inputs. Let's create features that will help it predict.

# Convert expirationDate to datetime objects, telling pandas the day comes first
data['expirationDate'] = pd.to_datetime(data['expirationDate'], dayfirst=True)
# Calculate time to expiry in years
data['time_to_expiry'] = (data['expirationDate'] - datetime.now()).dt.days / 365.25

# We can't use the 'type' (call/put) directly, so let's convert it to a number.
# 1 for 'call', 0 for 'put'. This is called one-hot encoding.
data['type_numeric'] = data['type'].apply(lambda x: 1 if x == 'call' else 0)

# We will assume a fixed stock price for calculating moneyness, as the yfinance
# data doesn't provide the underlying price for each option record.
# In a real-world scenario, you'd fetch this dynamically.
ASSUMED_STOCK_PRICE = 170.0  # Placeholder for AAPL stock price
data['moneyness'] = ASSUMED_STOCK_PRICE / data['strike_price']

# --- 3. Prepare Data for Modeling ---

# Define our features (X) and the target (y)
features = ['time_to_expiry', 'strike_price', 'moneyness', 'type_numeric']
target = 'implied_volatility'

X = data[features]
y = data[target]

# Split the data into a training set (to teach the model) and a testing set (to evaluate it)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

print(f"Training data shape: {X_train.shape}")
print(f"Testing data shape: {X_test.shape}")


# --- 4. Train the Model ---

# We'll use a RandomForestRegressor. It's a powerful and versatile model.
print("Training the model...")
model = RandomForestRegressor(n_estimators=100, random_state=42, n_jobs=-1)

# Fit the model to our training data
model.fit(X_train, y_train)
print("Model training complete.")


# --- 5. Evaluate the Model ---

# Make predictions on the unseen test data
predictions = model.predict(X_test)
mse = mean_squared_error(y_test, predictions)
print(f"Model evaluation on test data:")
print(f"Mean Squared Error (MSE): {mse:.6f}")
print(f"Root Mean Squared Error (RMSE): {np.sqrt(mse):.6f}")


# --- 6. Save the Trained Model ---

# We'll save the trained model to a file using joblib for later use in our API.
model_filename = 'volatility_model.joblib'
joblib.dump(model, model_filename)

print(f"Model saved to {model_filename}") 
