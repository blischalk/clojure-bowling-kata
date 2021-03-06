(ns bowling.core)


(defn score
  [])


(defn ^:dynamic roll
  ([previous-pin-count] (rand-nth 
               (range (- 11 previous-pin-count)))))


(defn roll-frame []
  (let [first-roll (roll 0)]
    (if (= 10 first-roll) [first-roll 0]
        [first-roll (roll 0)])))


(defn roll-tenth-frame
  [] (let [first-roll (roll 0)
           second-roll (roll (if (< first-roll 10) first-roll 0))]
       (cond 
         ;; two strikes
         (= 10 first-roll second-roll)
         [first-roll second-roll (roll 0)]

         ;; spare
         (and (= 10 (+ first-roll second-roll))
              (not= 10 first-roll second-roll))
         [first-roll second-roll (roll second-roll)]

         ;; no third ball
         :otherwise [first-roll second-roll 0])))


(defn roll-game []
  (conj (vec (repeatedly 9 roll-frame)) (roll-tenth-frame)))


(defn score-game [game]
  (let [partitioned (partition-all 3 1 game)]
    (reduce (fn [coll frame-block]
              (let [cframe (first frame-block)
                    nframe (first (rest frame-block))
                    lframe (first (rest (rest frame-block)))
                    pre-total (apply + cframe)]
                (conj coll 
                      (cond (and (= 10 (+ (first cframe) (second cframe)))
                                 (not= 10 (first cframe)))
                            (if (nil? nframe)
                              pre-total
                              (+ pre-total (first nframe)))
                            (= 10 (first cframe) (second cframe))
                            pre-total
                            (= 10 (first cframe))
                            (+ pre-total
                               (first nframe) 
                               (if (= 10 (first nframe))
                                 (first lframe)
                                 (last nframe)))
                            :otherwise pre-total))))
            []
            partitioned)))


(defn -main
  "The bowling game"
  [])

