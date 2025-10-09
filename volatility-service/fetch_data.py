import yfinance as yf
import pandas as pd

# --- Configuration ---
# Let's use a well-known tech stock, e.g., Apple Inc.
TICKER = 'AAPL'

# --- Fetching the Data ---

# Create a Ticker object
stock = yf.Ticker(TICKER)

# Get all available option expiration dates
expiration_dates = stock.options

print(f"Fetching option chain data for {TICKER}...")

# Create an empty list to store data from all expiry dates
all_options_data = []

# Loop through each expiration date to get the option chain
for date in expiration_dates:
    # Get the option chain for the specific date
    option_chain = stock.option_chain(date)
    
    # Combine call and put options into a single DataFrame
    # and add the expiration date as a column
    calls = option_chain.calls
    calls['type'] = 'call'
    puts = option_chain.puts
    puts['type'] = 'put'
    
    options_for_date = pd.concat([calls, puts])
    options_for_date['expirationDate'] = date
    
    all_options_data.append(options_for_date)

# Combine all DataFrames from the list into one
if all_options_data:
    final_data = pd.concat(all_options_data, ignore_index=True)

   # --- Data Cleaning and Feature Engineering ---
    relevant_columns = [
        'strike',
        'impliedVolatility',
        'type',
        'expirationDate'
    ]
    # Keep only the columns we need
    final_data = final_data[relevant_columns]

    # **NEW STEP: Filter out rows with near-zero implied volatility**
    # This removes the "bad" data points we observed.
    final_data = final_data[final_data['impliedVolatility'] > 0.001]

    # Rename columns for clarity
    final_data = final_data.rename(columns={
        'strike': 'strike_price',
        'impliedVolatility': 'implied_volatility',
    })

    # Save the cleaned data to a CSV file
    output_path = 'real_options_data.csv'
    final_data.to_csv(output_path, index=False)

    print(f"Successfully fetched, cleaned, and saved {len(final_data)} option contracts to {output_path}")



 
