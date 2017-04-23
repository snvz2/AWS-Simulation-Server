'use strict';
const AWS = require('aws-sdk');
const docClient = new AWS.DynamoDB.DocumentClient({region: 'us-east-1'});

exports.handler = function index(event, context, callback){
    
    let avgRating = 0;
    let truckIdToUpdate = '6';
    let updateParams;
   
   let scanningParameters = {
       TableName: 'Trucks',
       Limit:100
   };
   
//   let printParameters = {
//       Item:{
//           totalRating: 1,
//           id: "5"
//       }
//       ,
//       TableName: "Trucks"
//   };
   


//   docClient.put(printParameters, (err,data) => {
//       if(err){
//           console.log(err);
//       }
//       console.log(data);
//       callback(null, data);
//   });

   
   docClient.scan(scanningParameters, function(err,data){
       if(err)
       {
           callback(err,null);
       }
       else{
            
            for(let x = 0; x < data.Items.length; x++){
                if(data.Items[x].rating){
                    
                    let totalRating = 0;
                    let ratingOne = data.Items[x].rating['1'] * 1;
                    let ratingTwo = data.Items[x].rating['2'] * 2;
                    let ratingThree = data.Items[x].rating['3'] * 3;
                    let ratingFour = data.Items[x].rating['4'] * 4;
                    let ratingFive = data.Items[x].rating['5'] * 5;
                    
                    for(let z = 1; z <= 5; z++){
                        totalRating += data.Items[x].rating[z];
                    }
                    
                    //console.log((ratingOne + ratingTwo + ratingThree + ratingFour + ratingFive) / totalRating);
                    truckIdToUpdate = data.Items[x].id;
                    avgRating = (ratingOne + ratingTwo + ratingThree + ratingFour + ratingFive) / totalRating;
                    
                    updateParams = {
                      TableName: 'Trucks',
                      Key: { id : truckIdToUpdate },
                      UpdateExpression: 'set #totalRating = :x',
                      ExpressionAttributeNames: {
                          "#totalRating": "avgRating"
                      },
                        ExpressionAttributeValues: {
                        ':x' : avgRating
                      }
                    };

                    documentClient.update(updateParams, function(err, data) {
                       if (err) console.log(err);
                       else console.log(data);
                    });

                }
            }
       }
   });
   
   
   let documentClient = new AWS.DynamoDB.DocumentClient();
}