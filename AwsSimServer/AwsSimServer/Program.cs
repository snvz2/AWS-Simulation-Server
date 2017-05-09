using System;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DocumentModel;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.IO;
using System.Xml;
using System.Text.RegularExpressions;

namespace AwsSimServer
{
    public partial class Program
    {
        public static Table GetTableObject(string tableName)
        {

            AmazonDynamoDBClient client;

            try
            {
                client = new AmazonDynamoDBClient("AKIAJSXUSFFBXHMXK7LA", "r1RkV4lXb0QOfnoW8O1ftgko+cXvHFyrqZkG/lSS", RegionEndpoint.USEast1);
                Console.WriteLine("Connected to AWS");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return (null);
            }
            // Now, create a Table object for the specified table
            Table table;
            try
            {
                table = Table.LoadTable(client, tableName);
                Console.WriteLine("Loaded Table");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                Console.WriteLine("Failed to Load Table");
                return (null);
            }

            return (table);
        }

        public static void ServeFood (string jsonData) //(string[] menu)
        {

            Console.WriteLine(jsonData);
            Console.WriteLine("\n\n\n==========================================================================");
            Console.WriteLine("==========================================================================");
            Console.WriteLine("             MOVING TO ANOTHER LOCATION and UPDATING MENU ");
            Console.WriteLine("==========================================================================");
            Console.WriteLine("==========================================================================\n\n\n");
            // get json data
            JObject o = JObject.Parse(jsonData.TrimStart('[').TrimEnd(']'));
            var name = o.SelectToken("name");
            var rating = o.SelectToken("avgRating");
            var drinks = o.SelectToken("Menu.Drink");
            var food = o.SelectToken("Menu.Food");
            Console.WriteLine("+++++++++++++++ '" + name + "' Truck +++++++++++++++\n\n"); 

            // serving food at each location for 30 seconds (30000 milsecs) then moving to a new location
            for (var i = 0; i < 7; i++)
            {
                Console.WriteLine("==========================================================================");
                Console.WriteLine("" + i + ". TRUCK '" + name +"': Serving Food Now. TIME: " + DateTime.Now.ToLongTimeString() + " ");
                Console.WriteLine("==========================================================================\n\n");
                Console.WriteLine("     RATING: " + rating);
                Console.WriteLine("\n\n     DRINKS: " + drinks);
                Console.WriteLine("\n\n     FOOD: " + food);
                System.Threading.Thread.Sleep(3000);
            }
        }
        static void Main(string[] args)
        {
            string agt_table = "Trucks";
            string jsonDirectory = @"..\..\json";
            
            /*
            *  Insert into DynamoDB using dynamodb profile
            */
            // JSON to DynamoDB write
            StreamReader sr = null;
            JsonTextReader jtr = null;
            JArray recordArray = null;
            // Read in the JSON data from the .json file
            for (;;)
            {
                var jsonFiles = Directory.EnumerateFiles(jsonDirectory, "*.json");
                foreach (string jsonFile in jsonFiles)
                {
                    Console.WriteLine(jsonFile);

                    try
                    {
                        sr = new StreamReader(jsonFile);
                        jtr = new JsonTextReader(sr);
                        recordArray = (JArray)JToken.ReadFrom(jtr);
                        Console.WriteLine(recordArray.ToString());
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine("\n Error: can not read from the JSON file, " + ex.Message);
                    }
                    finally
                    {
                        if (jtr != null)
                            jtr.Close();
                        if (sr != null)
                            sr.Close();
                        Console.WriteLine("\n finally");
                    }

                    // Get a Table object for the table that created in Step 1
                    Table table = GetTableObject(agt_table);
                    if (table == null)
                    {
                        Console.WriteLine("\n Error getting GetTableObject");
                        goto PauseForDebugWindow;
                    }
                    else
                    {
                        Console.WriteLine("\n getting GetTableObject");
                    }
                    // Load the data into the table (this could take some time)
                    Console.Write("\n   Now writing {0:#,##0} records from JSON (might take 15 minutes)...\n   ...completed: ", recordArray.Count);
                    for (int y = 0, j = 99; y < recordArray.Count; y++)
                    {
                        try
                        {
                            Console.WriteLine("\n put item");
                            string itemJson = recordArray[y].ToString();
                            Document doc3 = Document.FromJson(itemJson);
                            table.PutItem(doc3);
                        }
                        catch (Exception ex)
                        {
                            Console.WriteLine("\nError: Could not write the record #{0:#,##0}, because {1}", y, ex.Message);
                            goto PauseForDebugWindow;
                        }
                        if (y >= j)
                        {
                            j++;
                            Console.Write("{0,5:#,##0}, ", j);
                            if (j % 1000 == 0)
                            {
                                Console.Write("\n                 ");
                            }
                            j += 99;
                        }
                    }
                    Console.WriteLine("\n   Finished updating DynamoDB!");
                    // Sleep or Move to a new location
                     ServeFood(recordArray.ToString());

                } // for loop
            } // infinite loop
        PauseForDebugWindow:
            Console.Write("\n\n Debug...Press any key");
            Console.ReadKey();
            Console.WriteLine();

            // keep
            while (true) { }
        }
    }
}
