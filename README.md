# CST-239 Assignment

## Screencast Video
Watch my full screencast demonstration here:

## Click to watch on Loom : https://www.loom.com/share/e9a1f84eda3840a59b10f9c6df61f69d


## How to Compile and Run

```bash
javac -d out $(Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out edu.gcu.storefront.StoreFrontApp