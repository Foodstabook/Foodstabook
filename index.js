// John Bower Foodstabook project Cloud Functions
// 02 Nov. 2022

// The Cloud Functions for Firebase SDK to create Cloud Functions and set up triggers.
const functions = require("firebase-functions");

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');
// Instantiate admin
admin.initializeApp(functions.config().firebase);

// // Instantiate an Algolia client
const algoliasearch = require('algoliasearch');
const algoliaClient = algoliasearch('I7YAJNGZX1', '3b8702d93f0d6460411d64940379a38d');
const index = algoliaClient.initIndex('Post');
// const algolia = algoliasearch(functions.config().algolia.appid, functions.config().algolia.adminkey);
// const index = algolia.initIndex('Post');

exports.getAlgoliaAppId = functions.https.onRequest((request, response) => {
    // Returns our Algolia Application Id used for our identity using Algolia API
    response.send("I7YAJNGZX1");
});

exports.getAlgoliaSrchKey = functions.https.onRequest((request, response) => {
    // Returns the search-only API key to be used in our front-end code
    response.send("3b8702d93f0d6460411d64940379a38d");
});

exports.searchTopHitOnly = functions.https.onRequest( async(request, response)=> {
    var query_str = request.body;
    var results = await index.search(query_str, {
        attributesToRetrieve: ['Title', 'Description', 'Username', 'UID'],
    }).then(({hits}) => {
        var result = hits[0];
        const responseData = {
            Title: result.Title,
            Description: result.Description
        }
        const jsonReturn = JSON.stringify(responseData);
        response.send(jsonReturn);
    });
});

exports.searchQuery = functions.https.onRequest( async(request, response)=> {
    const query_str = request.body;
    const search_results = [];
    const results = await index.search( query_str, {
        attributesToRetrieve: ['Title', 'Description', 'Username', 'UID'],
        hitsPerPage: 50,
    }).then(({hits}) => {
        const len = hits.length;
        for(let i = 0; i < len; i++) {
            let post_obj = {
                Title: hits[i].Title,
                Description: hits[i].Description,
                Username: hits[i].Username,
                UID: hits[i].UID
            }
            search_results.push(post_obj);
        }
        response.send(JSON.stringify(search_results));
    });
});
