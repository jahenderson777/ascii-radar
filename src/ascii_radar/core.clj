(ns ascii-radar.core
  (:require [clojure.string :as str])
  
  (:gen-class))

(def known-invaders
  ["
--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---"
   
   "
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o"
   ])

;; Your Clojure application must take a radar sample as an argument and reveal possible locations of those pesky invaders.


;; so I guess we need to scan over a given radar sample, shifting a 'window' around the same size of each invader,
;; seeing how closely that 'window' matches a known invader, generating like a score for how closely we get a match.

;; we'll do the scanning over the whole sample later, let's first tackle being given a window that is the same size
;; as our known invader.

;; other thoughts: when dealing with the edges we might want to have a third state of a grid position being 'unknown',
;; meaning this is the outside of the radar.


(defn -main [& args]
  )

