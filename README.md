# Subsystem

[Codox](http://tcsavage.github.io/docs/subsystem/0.1.0-SNAPSHOT/)

Provides a facility to package a system map as a component in a larger system.

## Usage

```
(subsystem
  (component/system-map
    :comp1 (component/using comp1 [:external-comp])
    :comp2 (component/using comp2 [:comp1 :external]))
  :post-start some-operation)
```

## License

Copyright © 2015 Tom Savage

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.