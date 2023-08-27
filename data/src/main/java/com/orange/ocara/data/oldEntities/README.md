# Framework  
You can find this layer called in many different ways. It basically encapsulates the interaction with the framework, so that the rest of the code can be agnostic and reusable in case you want to implement the same App in another platform. With framework I’m not only referring to the Android framework here, but to any external libraries that we want to able to change easily in the future.
  
For instance, if the data layer needs to persist something, here you could use Room to do it. Or if it needs to do a request, you would use Retrofit. Or it can access the sensors to request some info. Whatever you need!
  
This layer should be as simple as possible, as all the logic should be abstracted into the data layer.