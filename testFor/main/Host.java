package main;

public class Host 
{
	void hostFun(Client client)
	{
		client.clientFun();
	}

}



class Client
{
	void clientFun() {};
}