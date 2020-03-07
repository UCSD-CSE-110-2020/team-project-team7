const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// checks to see if there has been a new user invited
exports.sendInvite = functions.firestore
   .document('Teams/{teamId}/Members/{memberId}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.name + ' has sent an invite',
           body: 'Please accept on WWR'
         },
         topic: context.params.teamId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('team invite has been sent! ' + document.name, response);
           return response;
         })
         .catch((error) => {
           console.log('team invite has an uh oh', error);
           return error;
         });
     }

     return "document was null or empty";
   });

// checks to see if the user declined the team request
exports.declineInvite = functions.firestore
   .document('Teams/{teamId}/Members/{memberId}')
   .onDelete((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.name + ' has declined the invite',
           body: 'team invite has been denied'
         },
         topic: context.params.teamId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('team decline has been sent! ' + document.name, response);
           return response;
         })
         .catch((error) => {
           console.log('team decline has an uh oh', error);
           return error;
         });
     }

     return "document was null or empty";
   });

