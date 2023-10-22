const express = require('express');
const axios = require('axios');
const cors = require('cors');
const app = express();
const OAuthToken = require('./ebay_oauth_token');
const port = 8080;
const ebayAppId = "HeZhou-resiki-PRD-172b2ae15-03fae87d";

app.use(cors());

app.use(express.urlencoded({ extended: true }));

app.get('/search', async (req, res) => {
    try {
        const endpoint = `https://svcs.ebay.com/services/search/FindingService/v1`;

        const keywords = req.query.keywords || 'apple';
        const postalcode = req.query.postalcode || "90007";
        const distance = req.query.distance || "10";
        const category = req.query.category;
        const New = req.query.new;
        const used = req.query.used;
        const unspecified = req.query.unspecified;
        const localPickup = req.query.localPickup;
        const freeShipping = req.query.freeShipping;

        const params = {
            "OPERATION-NAME": "findItemsAdvanced",
            "SERVICE-VERSION": "1.0.0",
            "SECURITY-APPNAME": ebayAppId,
            "RESPONSE-DATA-FORMAT": "JSON",
            "REST-PAYLOAD": "true",
            "paginationInput.entriesPerPage": "50",
            "keywords": keywords,
            "buyerPostalCode": postalcode,
        };

        if (category !== "0") {
            params[`categoryId`] = category;
        }

        params[`itemFilter(0).name`] = "MaxDistance";
        params[`itemFilter(0).value`] = distance;

        let num = 1;
        if (!unspecified && (New || used)) {
            let count = 0;
            params[`itemFilter(${num}).name`] = "Condition";
            if (New) {
                params[`itemFilter(${num}).value(${count})`] = "New";
                count++;
            }
            if (used) {
                params[`itemFilter(${num}).value(${count})`] = "Used";
            }
            num++;
        }

        if (freeShipping) {
            params[`itemFilter(${num}).name`] = "FreeShippingOnly";
            params[`itemFilter(${num}).value`] = "true";
            num++;
        }

        if (localPickup) {
            params[`itemFilter(${num}).name`] = "LocalPickupOnly";
            params[`itemFilter(${num}).value`] = "true";
            num++;
        }

        params[`outputSelector(0)`] = "SellerInfo";
        params[`outputSelector(1)`] = "StoreInfo";

        const response = await axios.get(endpoint, { params });

        if (response.status === 200) {
            res.json(response.data);
        } else {
            res.status(500).json({ error: `Failed to retrieve data. Status code: ${response.status}` });
        }
    } catch (error) {
        res.status(500).json({ error: `An error occurred: ${error.message}` });
    }
});

app.get('/detail', async (req, res) => {
    console.log("detail");
    const client_id = "HeZhou-resiki-PRD-172b2ae15-03fae87d";
    const client_secret = "PRD-72b2ae15be14-5227-4df0-b4b3-9f40";

    const oauthToken = new OAuthToken(client_id, client_secret);

    const token = await oauthToken.getApplicationToken();

    try {
        const endpoint = "https://open.api.ebay.com/shopping";

        const headers = {
            "X-EBAY-API-IAF-TOKEN": token
        };

        const itemId = req.query.itemId || "335007021375";

        const params = {
            "callname": "GetSingleItem",
            "responseencoding": "JSON",
            "appid": ebayAppId,
            "siteid": "0",
            "version": "967",
            "ItemID": itemId,
            "IncludeSelector": "Description,Details,ItemSpecifics",
        };

        const response = await axios.get(endpoint, { params, headers });

        if (response.status === 200) {
            res.json(response.data);
        } else {
            res.status(500).json({ error: `Failed to retrieve data. Status code: ${response.status}` });
        }
    } catch (error) {
        res.status(500).json({ error: `An error occurred: ${error.message}` });
    }
});

app.get('/photo', async (req, res) => {
    console.log("photo");

    try {
        const endpoint = "https://www.googleapis.com/customsearch/v1"

        const apiKey = "AIzaSyASUztzWnFBg_O9MpQrAupASUGM-f603NE";
        const engineID = "73cb99b44c16e4668";

        const productTitle = req.query.productTitle || "iPhone14";

        const params = {
            "q": productTitle,
            "cx": engineID,
            "imgSize": "huge",
            "num": "8",
            "searchType": "image",
            "key": apiKey
        }

        const response = await axios.get(endpoint, { params });

        if (response.status === 200) {
            res.json(response.data);
        } else {
            res.status(500).json({ error: `Failed to retrieve data. Status code: ${response.status}` });
        }
    } catch (error) {
        res.status(500).json({ error: `An error occurred: ${error.message}` });
    }
});

app.get('/similar', async (req, res) => {
    try {
        const endpoint = `https://svcs.ebay.com/MerchandisingService`
        
        const itemId = req.query.itemId || "335007021375";

        const params = {
            "OPERATION-NAME": "getSimilarItems",
            "SERVICE-NAME": "MerchandisingService",
            "SERVICE-VERSION": "1.1.0",
            "CONSUMER-ID": ebayAppId,
            "RESPONSE-DATA-FORMAT": "JSON",
            "REST-PAYLOAD": "true",
            "itemId": itemId,
            "maxResults": "20"
        }

        const response = await axios.get(endpoint, { params });

        if (response.status === 200) {
            res.json(response.data);
        } else {
            res.status(500).json({ error: `Failed to retrieve data. Status code: ${response.status}` });
        }
    } catch (error) {
        res.status(500).json({ error: `An error occurred: ${error.message}` });
    }
});

app.get('/IPSuggest', async (req, res) => {
    try {
        const url = `http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=${req.query.IP}&maxRows=5&username=wodaxinoyume&country=US`

        const response = await axios.get(url);

        if (response.status === 200) {
            res.json(response.data);
        } else {
            res.status(500).json({ error: `Failed to retrieve data. Status code: ${response.status}` });
        }
    } catch (error) {
        res.status(500).json({ error: `An error occurred: ${error.message}` });
    }
});

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});

// establish connect with mongodb
const { MongoClient, ServerApiVersion } = require('mongodb');
const uri = "mongodb+srv://resiki:zh991001@cluster0.ci3kqxx.mongodb.net/?retryWrites=true&w=majority";

const client = new MongoClient(uri, {
    serverApi: {
        version: ServerApiVersion.v1,
        strict: true,
        deprecationErrors: true,
    }
});

async function initializeDatabase() {
    try {
        await client.connect();
        const db = client.db("mydb");
        const collection = db.collection("favorite");

        await collection.createIndex({ _id: 1 });

        console.log("Index created successfully");

    } catch (err) {
        console.error('Error initializing the database:', err);
    }
}

initializeDatabase();

app.get('/addItem', async (req, res) => {
    try {
        const encodedJsonString = req.query.object;
        const jsonString = decodeURIComponent(encodedJsonString);
        const myobj = JSON.parse(jsonString);

        console.log(myobj);

        const db = client.db("mydb");
        const result = await db.collection("favorite").insertOne(myobj);

        console.log("1 document inserted");
        res.status(200).json({ message: 'Document inserted successfully' });
    } catch (err) {
        console.error('Error inserting document:', err);
        res.status(500).json({ error: {err} });
    }
});

app.get('/deleteItem', async (req, res) => {
    try {
        const encodedJsonString = req.query.object;
        const jsonString = decodeURIComponent(encodedJsonString);
        const myobj = JSON.parse(jsonString);

        const db = client.db("mydb");
        const result = await db.collection("favorite").deleteOne(myobj);

        if (result.deletedCount === 1) {
            console.log("1 document deleted");
            res.status(200).json({ message: 'Document deleted successfully' });
        } else {
            console.error('Document not found');
            res.status(404).json({ error: 'Document not found' });
        }
    } catch (err) {
        console.error('Error deleting document:', err);
        res.status(500).json({ error: 'Database operation error' });
    }
});

app.get('/findAll', async (req, res) => {
    try {
        const myobj = {};
        const db = client.db("mydb");
        const result = await db.collection("favorite").find(myobj).toArray();

        console.log("Documents found");
        res.status(200).json({ message: 'Documents found successfully', data: result });
    } catch (err) {
        console.error('Error accessing the database:', err);
        res.status(500).json({ error: 'Database operation error' });
    }
});

app.get('/findAllId', async (req, res) => {
    try {
        const myobj = {};
        const db = client.db("mydb");
        const result = await db.collection("favorite").find(myobj, { projection: { _id: 1 } }).toArray();

        console.log("Documents found");
        res.status(200).json({ message: 'Documents found successfully', data: result });
    } catch (err) {
        console.error('Error accessing the database:', err);
        res.status(500).json({ error: 'Database operation error' });
    }
});

// Handle application exit to close the database connection
process.on('SIGINT', () => {
    client.close().then(() => {
        console.log('Closed the database connection');
        process.exit();
    });
});