const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// checks to see if there has been a new user invited
exports.sendInvite = functions.firestore
   .document('topic/{topicId}/messages/{message}')
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
         data:{
           act: 'invite_page',
           email: document.email
         },
         topic: context.params.topicId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('team invite has been sent! ' + document.name + ' ' + document.email, response);
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
   .document('topic/{topicId}/messages/{message}')
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
         topic: context.params.topicId
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


// checks to see if the user declined the team request
exports.teamUpdate = functions.firestore
   .document('topic/{topicId}/messages/{message}')
   .onUpdate((change, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = change.after.exists ? change.after.data() : null;

     if (document) {
        var notif = 'placeholder';
        if(document.notif == 1){
            notif = 'bad time';
        }else if(document.notif == 2){
            notif = 'bad route';
        }else if (document.notif == 3){
            notif = 'accept';
        }

        if(notif === 'placeholder'){ return "document was pending";}


       var message = {
         notification: {
           title: document.name + ' has updated their status: ' + notif,
           body: 'check the proposed walks page for more details'
         },
         data:{
            act: 'proposed_details'
         },
         topic: context.params.topicId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('team Update has been sent! ' + document.name, response);
           return response;
         })
         .catch((error) => {
           console.log('team Update has an uh oh', error);
           return error;
         });
     }

     return "document was null or empty";
   });


exports.proposedWalk = functions.firestore
   .document('topic/{topicId}/messages2/{message}')
   .onWrite((change, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = change.after.exists ? change.after.data() : null;

     if (document) {

       var message = {
         notification: {
           title: 'the creator has ' + document.notify + ' the walk',
           body: 'check the scheduled walks page for more details'
         },
         data:{
            act: 'proposed_details'
         },
         topic: context.params.topicId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('team propose has been sent! ' + document.notify, response);
           return response;
         })
         .catch((error) => {
           console.log('team propose has an uh oh', error);
           return error;
         });
     }

     return "document was null or empty";
   });

