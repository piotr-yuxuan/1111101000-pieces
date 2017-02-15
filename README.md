# 1111101000-pieces

Le mille pattes faisait un mille pièces.

## Artistry

… to be written …

## Distribution

```
lein dist
```

Distributable files with be placed within `docs/` where GitHub pages look them for.

There are no such thing like version numbers but commit hashes are used to refer different versions.

## Documentation

Additional documentation will be written under `docs/doc` if ever.

## Development

Technically, this project is a [re-frame](https://github.com/Day8/re-frame) rich single-paged application built upon [React](https://facebook.github.io/react/) (wrapped into [Reagent](http://reagent-project.github.io/)) and [garden](https://github.com/noprompt/garden). I use [Figwheel](https://github.com/bhauman/lein-figwheel), [`lein-garden`](https://github.com/noprompt/lein-garden) and [Dirac](https://github.com/binaryage/dirac) for development so I can enjoy [live-coding](https://en.wikipedia.org/wiki/Live_coding). For the time being, this project is distributed within a [GitHub page](https://pages.github.com/) : [https://piotr-yuxuan.github.io/1111101000-pieces/](https://piotr-yuxuan.github.io/1111101000-pieces/)

### Update third party code

``` bash
lein ancient upgrade :all :check-clojure :allow-all
```

Required anytime Chrome Canary (hence Dirac) get new version numbers.

### Clean project state

``` bash
lein clean
```

### Automatically reload hot code and css

``` bash
lein reload
```

Local developement endpoint is accessible via [http://localhost:3450](http://localhost:3450).

### Dirac

Start Dirac agent in a Java REPL

``` bash
lein repl
```

You may prefer to launch latest Chrome Canary with a dedicated user profile:

``` bash
cd ~ # anywhere else
/Applications/Google\ Chrome\ Canary.app/Contents/MacOS/Google\ Chrome\ Canary \
  --remote-debugging-port=9222 \
  --no-first-run \
  --user-data-dir=.chrome-user-data-dir
```

Install [Dirac Chrome extension](https://chrome.google.com/webstore/detail/dirac-devtools/kbkdngfljkchidcjpnfcgcokkbhlkogi) ifneedsbe.
