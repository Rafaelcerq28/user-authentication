"# user-authentication" 

- Add
/user
{
	username;
	password;
	role; ROLE_USER
}

- Get
/get/id

- GetAll
/user

- Delete
/user/id

- Register
/register
{
	username;
	password;
	role; ROLE_USER
}

- Login
{
	username;
	password;
}

---------------------------------

-Add
/item
{
	itemName;
	price;
	stock;
}

- Get
/item/id

- GetAll
/item

- Delete
/item/id