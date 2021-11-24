# graphqlpocclient

postman api for client

Post call http://localhost:8090/client/invokeApi

body
{
    "query":"query{customers(count: 2) { cardType,firstName,lastName,email}}"
}
