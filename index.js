// The Cloud Functions for Firebase SDK to create Cloud Functions and set up triggers.
const functions = require('firebase-functions');

// get Firestore
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');

// get Algolia for search functions
const algoliasearch = require('algoliasearch')
// initialize the Algolia Client with App id and API Key
const client = algoliasearch('I7YAJNGZX1', 'a3a86e37fc08823f5df74ff43befa9dc');
// initialize an index for posts
const index = client.initIndex('post_search');

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');
admin.initializeApp();

// get auth and setup auth emulator
// // const auth = firebase.auth();
// const auth = admin.auth();
// connectAuthEmulator(auth, "http://localhost:9099");

const Debug = true;

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

// Respond to http request with a random number between 1-100
exports.randomNumber = functions.https.onRequest((request, response) => {
    const number = Math.round(Math.random() * 100);
    response.send(number.toString());
});

/**
 * Not My Code!!! From github.com/firebase/functions-sample
 * Send an account deleted email confirmation to users who delete their accounts.
 */
// [START onDeleteTrigger]
exports.sendByeEmail = functions.auth.user().onDelete((user) => {
    // [END onDeleteTrigger]
      const email = user.email;
      const displayName = user.displayName;
    
      return sendGoodbyeEmail(email, displayName);
    });
    // [END sendByeEmail]

// [START sendWelcomeEmail]
/** Not My Code!!! From github.com/firebase/functions-sample
 * Sends a welcome email to new user.
 */
// [START onCreateTrigger]
exports.sendWelcomeEmail = functions.auth.user().onCreate((user) => {
    // [END onCreateTrigger]
      // [START eventAttributes]
      const email = user.email; // The email of the user.
      const displayName = user.displayName; // The display name of the user.
      // [END eventAttributes]
    
      return sendWelcomeEmail(email, displayName);
    });
    // [END sendWelcomeEmail]

// from tutorial Firebase Cloud 
exports.addMessage = functions.https.onCall(async(data, context) => {
    const uid = checkAuth(data, context);
    if(uid != null) {
        const msgText = data.msgText;
    const docRef = admin.firestore().collection("messages").doc();

    // async
    const writeResult = await docRef.set({
        docid: docRef.id,
        msgText: msgText,
        uid: uid,
        createdAt: admin.firestore.FieldValue.serverTimestamp(), 
    });

    return docRef.id;
    } else {
        throw new functions.https.HttpsError(
            "unauthenticated",
            "User not authenticated !! Please login !!"
        )
    }
    return null;
})

const checkAuth = (data, context) => {
    if(Debug) {
        if (data.uid) {
            return data.uid;
        }
    } else {
        if (context.auth) {
            return context.auth.uid;
        }
    }
    return null;
};

// addMessage({ msgText: "Hello World", uid: "kwoxAsTghXPCrvQVPdJ6b4tw2Rs2"});

exports.getMessages = functions.https.onCall(async (data, context) => {
    const uid = checkAuth(data, context);
    if(uid != null) {
        const readResult = await admin.firestore().collection("messages").get();

        console.log(readResult.docs);
        return null;
    } else {
        throw new functions.https.HttpsError(
            "unauthenticated",
            "User not authenticated !! Please login !!");
    }
    return null;
});

// firebase.auth().signInWithCredential(firebase.auth.GoogleAuthProvider.credential(
//     '{"sub": "abc123", "email": "foo@example.com", "email_verified": true}'
// end above tutorial. Run in shell with:
//getMessages({ uid: "kwoxAsTghXPCrvQVPdJ6b4tw2Rs2"});

// Algolia Search, add posts to algolia as create
exports.indexPost = functions.firestore.document('Post/{UID}').onCreate((snap, context) => {
        const data = snap.data(); 
        const objectId = snap.id; // make algolia object id same as firestore id

        return index.addObject({
            objectId,
            ...data
        });
});

// Make sure to delete objects from Algolia as they are deleted from Firestore
exports.unindexPost = functions.firestore.document('Post/{UID}').onDelete((snap, context) => {
    const objectId = snap.id;
    return index.deleteObject(objectId);
});

