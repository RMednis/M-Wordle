# M-Wordle

Spēle tika izveidota priekš RTU Datortehnikas un Automātikas 2. Kursa Android izstrādes priekšmeta.

Vārdnīcas priekš ievades tika lejupielādētas un filtrētas no Tēzurs skaidrojošās un sinonīmu vardnīcas,
ko nodrošina Latvijas Universitātes Makslīgā Intelekta Laborotorija (LUMII)

Dati pieejami šeit: https://github.com/LUMII-AILab/Tezaurs
Filtrēšana tika daļēji veikta izmantojot tēzurs nodrošināto API, kas pieejams šeit: https://api.tezaurs.lv/

Liels paldies, bez šiem resursiem šo spēli nevarētu izveidot <3

Paša nejaušo vārdu ģenerēšana izmanto mazāku vārdnīcu, kas tika filtrēta manuāli, izvēloties vārdus kas ir 
labāk pazīstami, nav tieši svešvārdi, utt. 

Viss kods priekš filtrēšanas ir pieejams iekš projekta `assets/filtering-scripts` mapes.
Tur arī ir pieejamas pašas ģenērātās vārdnīcas, ko spēle izmanto.


Atskaitot to, spēle arī izmanto sekojošās ārējās bibliotēkas:
- Konfetti [https://github.com/DanielMartinus/Konfetti]


Kas ir licensētas zem sekojošām licensēm:

```
Konfetti

ISC License

Copyright (c) 2017 Dion Segijn

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.


```
