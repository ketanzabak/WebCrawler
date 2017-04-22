# WEB CRAWLER USING HADOOP MAP REDUCE .

Simple program which takes input from Input/test_input file .  It uses hadoop Map Reduce to map these URLs to Mapper.
Mapper reads each line and send request using Apache HTTPClient and stores response in separate file . LinkReducer then parses each file and extracts all the links from it . These links are appended to test_input so that in the next run these links will be used for processing .
