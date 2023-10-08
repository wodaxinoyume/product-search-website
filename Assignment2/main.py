# Copyright 2018 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# [START gae_python38_app]
# [START gae_python3_app]
from flask import Flask, jsonify, request
from flask_cors import CORS
import requests
from ebay_oauth_token import OAuthToken

client_id = "HeZhou-resiki-PRD-172b2ae15-03fae87d"

client_secret = "PRD-72b2ae15be14-5227-4df0-b4b3-9f40"

# Create an instance of the OAuthUtility class
oauth_utility = OAuthToken(client_id, client_secret)

# Get the application token
application_token = oauth_utility.getApplicationToken()

# If `entrypoint` is not defined in app.yaml, App Engine will look for an app
# called `app` in `main.py`.
app = Flask(__name__, static_url_path='', static_folder='.')

CORS(app)

ebay_app_id = "HeZhou-resiki-PRD-172b2ae15-03fae87d"

@app.route('/')
def index():
    return app.send_static_file('index.html')

@app.route('/search', methods=['GET'])
def search_ebay():
    try:
        endpoint = "https://svcs.eBay.com/services/search/FindingService/v1"

        keywords = request.args.get('keywords', default='apple')
        sortorder = request.args.get('sortby')
        MinPrice = request.args.get('MinPrice')
        MaxPrice = request.args.get('MaxPrice')
        Condition1 = request.args.get('Condition1')
        Condition2 = request.args.get('Condition2')
        Condition3 = request.args.get('Condition3')
        Condition4 = request.args.get('Condition4')
        Condition5 = request.args.get('Condition5')
        ReturnAccepted = request.args.get('ReturnAcceptedOnly')
        FreeShippingOnly = request.args.get('FreeShippingOnly')

        params = {
            "OPERATION-NAME": "findItemsAdvanced",
            "SERVICE-VERSION": "1.0.0",
            "SECURITY-APPNAME": ebay_app_id,
            "RESPONSE-DATA-FORMAT": "JSON",
            "REST-PAYLOAD": "true",
        }

        params["keywords"] = keywords
        params["sortOrder"] = sortorder

        num = 0
        if MinPrice:
            params[f"itemFilter({num}).name"] = "MinPrice"
            params[f"itemFilter({num}).value(0)"] = MinPrice
            params[f"itemFilter({num}).paramName"] = "Currency"
            params[f"itemFilter({num}).paramValue"] = "USD"
            num += 1

        if MaxPrice:
            params[f"itemFilter({num}).name"] = "MaxPrice"
            params[f"itemFilter({num}).value(0)"] = MaxPrice
            params[f"itemFilter({num}).paramName"] = "Currency"
            params[f"itemFilter({num}).paramValue"] = "USD"
            num += 1
        
        if Condition5 or Condition4 or Condition3 or Condition2 or Condition1:
            params[f"itemFilter({num}).name"] = "Condition"
            num += 1

        count= 0
        if Condition1:
            params[f"itemFilter({num - 1}).value({count})"] = "1000"
            count += 1

        if Condition2:
            params[f"itemFilter({num - 1}).value({count})"] = "3000"
            count += 1

        if Condition3:
            params[f"itemFilter({num - 1}).value({count})"] = "4000"
            count += 1

        if Condition4:
            params[f"itemFilter({num - 1}).value({count})"] = "5000"
            count += 1

        if Condition5:
            params[f"itemFilter({num - 1}).value({count})"] = "6000"
            count += 1

        if ReturnAccepted:
            params[f"itemFilter({num}).name"] = "ReturnsAcceptedOnly"
            params[f"itemFilter({num}).value(0)"] = ReturnAccepted
            num += 1
        
        if FreeShippingOnly:
            params[f"itemFilter({num}).name"] = "FreeShippingOnly"
            params[f"itemFilter({num}).value(0)"] = FreeShippingOnly
            num += 1


        response = requests.get(endpoint, params=params)

        if response.status_code == 200:
            return response.json()
        else:
            return jsonify({"error": f"Failed to retrieve data. Status code: {response.status_code}"}), 500

    except requests.exceptions.RequestException as e:
        return jsonify({"error": f"Request error: {e}"}), 500
    except Exception as e:
        return jsonify({"error": f"An error occurred: {e}"}), 500
    
@app.route('/detail', methods=['GET'])
def search_detail():
    try:
        endpoint = "https://open.api.ebay.com/shopping?"

        headers = {
            "X-EBAY-API-IAF-TOKEN": oauth_utility.getApplicationToken()
        }

        itemId = request.args.get('itemId', default = "335007021375")

        params = {
           "callname": "GetSingleItem",
           "responseencoding": "JSON",
           "appid": ebay_app_id,
           "siteid": "0",
           "version": "967",
           "ItemID": itemId,
           "IncludeSelector": "Description,Details,ItemSpecifics",
        }

        response = requests.get(endpoint, params=params, headers=headers)

        if response.status_code == 200:
            return response.json()
        else:
            return jsonify({"error": f"Failed to retrieve data. Status code: {response.status_code}"}), 500

    except requests.exceptions.RequestException as e:
        return jsonify({"error": f"Request error: {e}"}), 500
    except Exception as e:
        return jsonify({"error": f"An error occurred: {e}"}), 500



if __name__ == "__main__":
    # This is used when running locally only. When deploying to Google App
    # Engine, a webserver process such as Gunicorn will serve the app. You
    # can configure startup instructions by adding `entrypoint` to app.yaml.
    app.run(host="127.0.0.1", port=8080, debug=True)
# [END gae_python3_app]
# [END gae_python38_app]
