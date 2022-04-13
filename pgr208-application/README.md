## notater

# argumenter for binding:
* Slipper søke igjennom alle elementer i layout hiearkiet for å finne riktig element med findById, 
  ( så O(n) mens binding er O(1) med sine direkte referanser(?) )
* type safe,
* findById er ikke null safe, finnes ikke viewet kræsjer appen

# diffutil
* Sørger for at kun ett element i en liste blir rendret ved en endring knyttet til recyclerview, og ikke alle elementer