using System;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DocumentModel;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.IO;
using System.Xml;


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
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return (null);
            }

            return (table);
        }
        static void Main(string[] args)
        {

            string agt_table = "Trucks";
            string jsonfile = "id4.json"; // path + args[i] + @".json";
            /*
               *
               *  Insert into DynamoDB using dynamodb profile
               * 
            */
            // JSON to DynamoDB write
            StreamReader sr = null;
            JsonTextReader jtr = null;
            JArray recordArray = null;
            // First, read in the JSON data from the DynamoDBInput.json file
            try
            {
                sr = new StreamReader(jsonfile);
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
                Console.WriteLine("\n OK getting GetTableObject");
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
            Console.WriteLine("\n   Finished writing all records to DynamoDB!");

        //
        PauseForDebugWindow:
            Console.Write("\n\n Debug...Press any key");
            Console.ReadKey();
            Console.WriteLine();

            // keep
            while (true) { }
        }
    }
}
