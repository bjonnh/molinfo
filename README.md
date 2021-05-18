# molinfo

A tiny service providing information about molecules.

It is fast, and provides client side and server side caching, it also compresses with gzip, providing blazing fast responses.

## How to use

Currently there is no full-in-docker build (this is coming)

You need a JVM (Ideally >15) and Docker installed then:

```shell
./gradlew installDist
docker-compose up
```

You now have a service on the port 8042 that can answer to queries like:

http://127.0.0.1:8042/molecule/smiles/CCC(C)N1C(=O)N(C=N1)C2=CC=C(C=C2)N3CCN(CC3)C4=CC=C(C=C4)OCC5COC(O5)(CN6C=NC=N6)C7=C(C=C(C=C7)Cl)Cl.svg

## Currently supported endpoints

- /molecule/smiles/{smiles}.svg
- /molecule/smiles/{smiles}/inchikey
- /molecule/smiles/{smiles}/exactmass
- /molecule/smiles/{smiles}/averagemass

## What's next

- more molecular properties
- …
