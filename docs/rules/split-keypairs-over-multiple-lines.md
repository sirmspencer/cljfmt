# `:split-keypairs-over-multiple-lines`

Puts one keypair per line. The value selects which maps the rule applies to:

| Value | Applies to |
| --- | --- |
| `nil` (default) | nothing |
| `:always` | any map |
| `:mixed` | maps whose keys span more than one line |
| `:multiline` | maps where some keypair spans more than one line |

Each value is a subset of the one above it, so `:always` splits everything
`:mixed` does, and `:mixed` splits everything `:multiline` does.

## A map on a single line

Only `:always` expands it.

```clojure
;; Input
{:a 1 :b 2}

;; :always
{:a 1
 :b 2}

;; :mixed, :multiline
{:a 1 :b 2}
```

## A map with keys spread unevenly across lines

`:multiline` leaves it alone, because no entry spans lines.

```clojure
;; Input
{:a 22222222222222 :b 2
 :c 3              :d 4}

;; :always, :mixed
{:a 22222222222222
 :b 2
 :c 3
 :d 4}

;; :multiline
{:a 22222222222222 :b 2
 :c 3              :d 4}
```

## A map with an entry that spans lines

All three values split the map, including the grid pair on the last line.

```clojure
;; Input
{:a [1
     2] :b 2
 :c 3   :d 4}

;; :always, :mixed, :multiline
{:a [1
     2]
 :b 2
 :c 3
 :d 4}
```

A nested map behaves the same way:

```clojure
;; Input
{:a {:x 2
     :y 777777} :b 2
 :c 3           :d 4}

;; :always, :mixed, :multiline
{:a {:x 2
     :y 777777}
 :b 2
 :c 3
 :d 4}
```

A keypair also spans lines when the value sits on the line below its key. It
makes no difference whether the newline falls inside the value or before it:

```clojure
;; Input
{:a
 1
 :b 2 :c 3}

;; :always, :mixed, :multiline
{:a
 1
 :b 2
 :c 3}
```

The pair that was already split stays split, because the rule only inserts
newlines, and the remaining pairs are put on their own lines. A comment between a
key and its value counts the same way.
