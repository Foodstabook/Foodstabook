// John.Bower@studnet.csulb.edu
// 14 Nov. 2022 - Foodstabook Project

const functions = require("firebase-functions");
const adim = require('firebase-admin');
adim.initializeApp();

// Using Aloglia Instant Search
const algoliasearch = require('algoliasearch');
const { request, response } = require("express");
const algoliaClient = algoliasearch('I7YAJNGZX1', '3b8702d93f0d6460411d64940379a38d');
const index = algoliaClient.initIndex('Post');
const index_lastmodified = algoliaClient.initIndex('Post_lastmodified_desc');

exports.searchPosts = functions.https.onRequest(async (request, response) => {
    if(!request.headers.authorization) {response.status(403).send('Unauthorized request');}
    const query_str = request.body;
    const search_results = [];
    const results = await index.search( query_str, {
        attributesToRetrieve: ['title', 'uid', 'path'],
        hitsPerPage: 50,
    }).then(({hits}) => {
        const len = hits.length;
        for(let i = 0; i < len; i++) {
            let post_obj = {
                uid: hits[i].uid,
                title: hits[i].title,
                path: hits[i].path,
                objectID: hits[i].objectID
            }
            search_results.push(post_obj);
        }
        response.send(JSON.stringify(search_results));
    });
});

exports.searchLatestPosts = functions.https.onRequest(async (request, response) => {
    if(!request.headers.authorization) {response.status(403).send('Unauthorized request');}
    const query_str = request.body;
    const search_results = [];
    const results = await index_lastmodified.search( query_str, {
        attributesToRetrieve: ['title', 'uid', 'path'],
        hitsPerPage: 50,
    }).then(({hits}) => {
        const len = hits.length;
        for(let i = 0; i < len; i++) {
            let post_obj = {
                uid: hits[i].uid,
                title: hits[i].title,
                path: hits[i].path,
                objectID: hits[i].objectID
            }
            search_results.push(post_obj);
        }
        response.send(JSON.stringify(search_results));
    });
});



exports.callSearchPosts = functions.https.onCall((data, context) => {
    if(!context.auth) { throw new functions.https.HttpsError('unauthenticated')};
    const queryString = data.searchQuery;
    const search_results = [];
    const results = index.search(queryString, {
        attributesToRetrieve: ['objectID','title', 'uid', 'path'],
        hitsPerPage: 50
    }).then((hits)=>{
        const len = hits.length;
        for(let i= 0; i < len; i++) {
            let post_obj = {
                uid: hits[i].uid,
                title: hits[i].title,
                path: hits[i].path,
                objectID: hits[i].objectID
            }
            search_results.push(post_obj);
        }
        return results;
    });
});

exports.callSearchLatestPosts = functions.https.onCall((data, context) => {
    if(!context.auth) { throw new functions.https.HttpsError('unauthenticated')};
    const queryString = data.searchQuery;
    const search_results = [];
    const results = index_lastmodified.search(queryString, {
        attributesToRetrieve: ['objectID','title', 'uid', 'path'],
        hitsPerPage: 50
    }).then((hits)=>{
        const len = hits.length;
        for(let i= 0; i < len; i++) {
            let post_obj = {
                uid: hits[i].uid,
                title: hits[i].title,
                path: hits[i].path,
                objectID: hits[i].objectID
            }
            search_results.push(post_obj);
        }
        return results;
    });
});