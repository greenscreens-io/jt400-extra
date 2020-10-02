
# [JT400 Extra Pack](https://greenscreens-io.github.io/jt400-extra/).

![Compile](https://github.com/greenscreens-io/jt400-extra/workflows/Compile/badge.svg?branch=master) 
![GitHub release (latest by date)](https://img.shields.io/github/v/release/greenscreens-io/jt400-extra?style=plastic)
![GitHub](https://img.shields.io/github/license/greenscreens-io/jt400-extra?style=plastic)

[![Maven Package](https://github.com/greenscreens-io/jt400-extra/workflows/Maven%20Package/badge.svg)](https://github.com/greenscreens-io/jt400-extra/packages) 
![GitHub Last Commit](https://img.shields.io/github/last-commit/greenscreens-io/jt400-extra?style=plastic)
![GitHub Release Date](https://img.shields.io/github/release-date/greenscreens-io/jt400-extra?style=plastic)


Green Screens JT400 Extension Pack - is a module on top of JT400 improving usage of JT400 ProgramCall class by helping to map IBM i program parameters and response formats. 

We were not happy with PCML approach as neither with writing a lot of manual specific code required for calling IBM i program, so we developed our own engine based on Java Dynamic Proxy and InvocationHandler technology. 

Instead of writing large XML mapping file or writing a lot of boilerplate code, imagine you can do something like this...

```
final QDCRDEVD params = QDCRDEVD.build(DEVD0100.class, "QPADEV0001");
final IQDCRDEVD program = IQDCRDEVD.create(as400);
final DEVD0100 res = program.call(params, DEVD0100.class);
		
System.out.println(res);
System.out.println(res.getDeviceName());
```

For more info about library, check out our blog [here](https://blog.greenscreens.io/green-screens-jt400-extension-pack).

&copy; Green Screens Ltd. 2016 - 2020
