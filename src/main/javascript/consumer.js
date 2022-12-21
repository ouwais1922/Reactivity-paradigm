const fs = require('fs');
const fetch = require('node-fetch'); // this is a third node module : npm i node-fetch
const prompt = require('prompt-sync')(); // this is a third node module : npm i prompt-sync

var url = 'http://localhost:8080/tools';

var myOption = 0;

async function myCall(){

    try{

        while(myOption != 4) {

            console.log('\n\t1. If you wanna get list of running proccesses press on : 1\n');
            console.log('\t2. if you wanna get the remote screenshot press on : 2\n');
            console.log('\t3. If you wanna reboot the remote system press on : 3 \n');
            console.log('\t4. Quit\n');

            myOption = prompt('\t Enter your option among the 4 options ==> ');

            // Reboot the remote system:
            if(myOption == 3) {
                await fetch(url + '/reboot').then((data) => {
                    return data.text();
                }).then((rs) => {
                    if (rs == "true") {
                        console.log('\n\tOperation Successful!');
                    }
                    else {
                        console.log('\n\tOperation Failed!');
                    }
                }).catch((error) => {
                    console.log(error);
                });
            }

            //Get remote screenshot:
            else if (myOption == 2) {
                await fetch(url + '/screenshot').then((value) => {
                    return value.text();
                }).then((data) => {
                    if(data == null){
                        console.log('\t Remote screenshot failed!');
                    }
                    else{
                        async function saveImage(filename, data){
                            var myBuffer = Buffer.from(data,"base64")
                            fs.writeFile('./remote/'+filename, myBuffer, function(err) {
                                if(err) {
                                    console.log(err.message);
                                } else {
                                    console.log("\t screenshot done successfully!");
                                }
                            });
                        }

                        let randomNumber = Math.floor(Math.random() * 100);

                        saveImage("screen"+randomNumber+ ".jpg", data);
                    }
                }).catch((error) => {
                    console.log(error);
                });
            }
            else if (myOption == 1){
                await fetch(url+ '/process').then((data)=>{
                    return data.text();
                }).then((values)=>{
   
                console.log(values);
                })
            }
        }

    }catch(err){
        console.log(err.message);
    }
}
myCall();