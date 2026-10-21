(ns cljfmt.rules.split-keypairs-over-multiple-lines-test
  (:require #?(:clj [cljfmt.test-util.clojure])
            [#?@(:clj (clojure.test :refer)
                 :cljs (cljs.test :refer-macros)) [deftest testing is are]]
            [cljfmt.test-util.common])
  #?(:cljs (:require-macros [cljfmt.test-util.cljs])))

(deftest test-split-keypairs
  (testing "off by default"
    (are [form] (reformats-to? form form {})
      ["{:a 1 :b 2}"]
      ["{:a 22222222222222 :b 2"
       " :c 3}"]
      ["{:a 22222222222222 :b 2"
       " :c 3              :d 4}"]
      ["{:a [1"
       "     2] :b 2"
       " :c 3   :d 4}"]
      ["{:a {:x 2"
       "     :y 777777} :b 2"
       " :c 3           :d 4}"]))
  (testing "true means :always"
    (are [opts] (reformats-to?
                 ["{:a 1 :b 2}"]
                 ["{:a 1"
                  " :b 2}"]
                 opts)
      {:split-keypairs-over-multiple-lines true}
      {:split-keypairs-over-multiple-lines? true})
    (are [opts] (reformats-to?
                 ["{:a 1 :b 2}"]
                 ["{:a 1 :b 2}"]
                 opts)
      {:split-keypairs-over-multiple-lines false}
      {:split-keypairs-over-multiple-lines? false}))
  (testing ":always"
    (is (reformats-to?
         ["{:a 1 :b 2}"]
         ["{:a 1"
          " :b 2}"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["{:a 22222222222222 :b 2"
          " :c 3}"]
         ["{:a 22222222222222"
          " :b 2"
          " :c 3}"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["{:a 22222222222222 :b 2"
          " :c 3              :d 4}"]
         ["{:a 22222222222222"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["{:a [1"
          "     2] :b 2"
          " :c 3   :d 4}"]
         ["{:a [1"
          "     2]"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["{:a {:x 2"
          "     :y 777777} :b 2"
          " :c 3           :d 4}"]
         ["{:a {:x 2"
          "     :y 777777}"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["(defn f [{:keys [aaa"
          "                 bbb"
          "                 ccc] :as p}])"]
         ["(defn f [{:keys [aaa"
          "                 bbb"
          "                 ccc]"
          "          :as p}])"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["{:a 1"
          " :b {:x 1 :y 2}}"]
         ["{:a 1"
          " :b {:x 1"
          "     :y 2}}"]
         {:split-keypairs-over-multiple-lines :always})
        "inline nested maps are split too")
    (are [form] (reformats-to? form form {:split-keypairs-over-multiple-lines :always})
      ["{:a {:x 1"
       "     :y 2}"
       " :b 2}"]
      ["{:a [1"
       "     2]}"]))
  (testing ":mixed"
    (are [form] (reformats-to? form form {:split-keypairs-over-multiple-lines :mixed})
      ["{:a 1 :b 2}"]
      ["{:a {:x 1"
       "     :y 2}"
       " :b 2}"]
      ["{:a [1"
       "     2]}"])
    (is (reformats-to?
         ["{:a 1"
          " :b {:x 1 :y 2}}"]
         ["{:a 1"
          " :b {:x 1 :y 2}}"]
         {:split-keypairs-over-multiple-lines :mixed})
        "an inline map inside a multi-line map stays inline")
    (is (reformats-to?
         ["{:a 22222222222222 :b 2"
          " :c 3}"]
         ["{:a 22222222222222"
          " :b 2"
          " :c 3}"]
         {:split-keypairs-over-multiple-lines :mixed}))
    (is (reformats-to?
         ["{:a 22222222222222 :b 2"
          " :c 3              :d 4}"]
         ["{:a 22222222222222"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :mixed}))
    (is (reformats-to?
         ["{:a [1"
          "     2] :b 2"
          " :c 3   :d 4}"]
         ["{:a [1"
          "     2]"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :mixed}))
    (is (reformats-to?
         ["{:a {:x 2"
          "     :y 777777} :b 2"
          " :c 3           :d 4}"]
         ["{:a {:x 2"
          "     :y 777777}"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :mixed}))
    (is (reformats-to?
         ["(defn f [{:keys [aaa"
          "                 bbb"
          "                 ccc] :as p}])"]
         ["(defn f [{:keys [aaa"
          "                 bbb"
          "                 ccc]"
          "          :as p}])"]
         {:split-keypairs-over-multiple-lines :mixed})))
  (testing ":multiline"
    (are [form] (reformats-to? form form {:split-keypairs-over-multiple-lines :multiline})
      ["{:a 1 :b 2}"]
      ["{:a 22222222222222 :b 2"
       " :c 3}"]
      ["{:a 22222222222222 :b 2"
       " :c 3              :d 4}"]
      ["{:a {:x 1"
       "     :y 2}"
       " :b 2}"]
      ["{:a [1"
       "     2]}"])
    (is (reformats-to?
         ["{:a [1"
          "     2] :b 2"
          " :c 3   :d 4}"]
         ["{:a [1"
          "     2]"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :multiline}))
    (is (reformats-to?
         ["{:a {:x 2"
          "     :y 777777} :b 2"
          " :c 3           :d 4}"]
         ["{:a {:x 2"
          "     :y 777777}"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :multiline}))
    (is (reformats-to?
         ["(defn f [{:keys [aaa"
          "                 bbb"
          "                 ccc] :as p}])"]
         ["(defn f [{:keys [aaa"
          "                 bbb"
          "                 ccc]"
          "          :as p}])"]
         {:split-keypairs-over-multiple-lines :multiline})))
  (testing ":multiline counts a value on the next line as a split keypair"
    (is (reformats-to?
         ["{:a"
          " 1 :b 2}"]
         ["{:a"
          " 1"
          " :b 2}"]
         {:split-keypairs-over-multiple-lines :multiline}))
    (is (reformats-to?
         ["{:a"
          " [1 2] :b 2}"]
         ["{:a"
          " [1 2]"
          " :b 2}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "a newline before a value counts the same as one inside it")
    (is (reformats-to?
         ["{:a"
          " 1"
          " :b 2 :c 3}"]
         ["{:a"
          " 1"
          " :b 2"
          " :c 3}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "the split pair is preserved; the remaining pairs are normalised")
    (is (reformats-to?
         ["{:a ;; note"
          " 1 :b 2}"]
         ["{:a ;; note"
          " 1"
          " :b 2}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "a comment between key and value also splits the pair")
    (is (reformats-to?
         ["{:a,"
          " 1 :b 2}"]
         ["{:a,"
          " 1"
          " :b 2}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "a comma before the newline is skipped, the newline is not")
    (is (reformats-to?
         ["{:a, 1 :b 2}"]
         ["{:a, 1 :b 2}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "a comma alone does not mean the pair is split")
    (testing "idempotency"
      (are [form] (reformats-to?
                   form form {:split-keypairs-over-multiple-lines :multiline})
        ["{:a"
         " 1"
         " :b 2}"]
        ["{:a"
         " 1"
         " :b 2"
         " :c 3}"]
        ["{:a ;; note"
         " 1"
         " :b 2}"])))
  (testing "multi-line keys are left alone"
    (are [mode] (reformats-to?
                 ["{{:a 1"
                  "  :b 2} 3"
                  " :c 5}"]
                 ["{{:a 1"
                  "  :b 2} 3"
                  " :c 5}"]
                 {:split-keypairs-over-multiple-lines mode})
      :always
      :mixed
      :multiline))
  (testing "comments and unevals"
    (is (reformats-to?
         ["{:one two #_comment :three four}"]
         ["{:one two #_comment"
          " :three four}"]
         {:split-keypairs-over-multiple-lines :always}))
    (is (reformats-to?
         ["{:one two #_comment :three four}"]
         ["{:one two #_comment :three four}"]
         {:split-keypairs-over-multiple-lines :mixed})
        "an uneval does not make the map multi-line")
    (is (reformats-to?
         ["{:one two ;comment"
          ":three four}"]
         ["{:one two ;comment"
          " :three four}"]
         {:split-keypairs-over-multiple-lines :mixed})
        "a key already preceded by a comment line is not split again")
    (is (reformats-to?
         ["{;comment"
          ":one two :three four}"]
         ["{;comment"
          " :one two"
          " :three four}"]
         {:split-keypairs-over-multiple-lines :mixed}))
    (is (reformats-to?
         ["{:one two ;comment"
          ":three four}"]
         ["{:one two ;comment"
          " :three four}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "a comment is not a multi-line entry")
    (is (reformats-to?
         ["{:a 1 #_{:x 1"
          "         :y 2} :b 2}"]
         ["{:a 1 #_{:x 1"
          "         :y 2}"
          " :b 2}"]
         {:split-keypairs-over-multiple-lines :multiline})
        "an uneval'd multi-line form is a multi-line entry, but is not a keypair"))
  (testing "idempotency"
    (are [mode form] (reformats-to? form form {:split-keypairs-over-multiple-lines mode})
      :always ["{:a 1"
               " :b 2}"]
      :always ["{:a 22222222222222"
               " :b 2"
               " :c 3"
               " :d 4}"]
      :mixed ["{:a [1"
              "     2]"
              " :b 2"
              " :c 3"
              " :d 4}"]
      :multiline ["{:a {:x 2"
                  "     :y 777777}"
                  " :b 2"
                  " :c 3"
                  " :d 4}"]
      :multiline ["(defn f [{:keys [aaa"
                  "                 bbb"
                  "                 ccc]"
                  "          :as p}])"]))
  (testing "with column alignment"
    (is (reformats-to?
         ["(defn f"
          "  [{:keys [first-option"
          "           second-option-longer"
          "           last?] :as opts}])"]
         ["(defn f"
          "  [{:keys [first-option"
          "           second-option-longer"
          "           last?]"
          "    :as   opts}])"]
         {:split-keypairs-over-multiple-lines :multiline
          :align-map-columns? true}))
    (is (reformats-to?
         ["{:a 22222222222222 :b 2"
          " :c 3              :d 4}"]
         ["{:a 22222222222222"
          " :b 2"
          " :c 3"
          " :d 4}"]
         {:split-keypairs-over-multiple-lines :mixed
          :align-map-columns? true}))))
