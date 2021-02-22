# Sentiment Analysis on News Texts
Sentiment Analysis on News Text determine the polarity of the news texts. The polarity of the news texts can be positive, negative  or neutral. Most of the news texts are neutral, while there can be positive and negative texts. We use Stanford Classifier with dictionary ethods on 5w answers to get the main poalrity of document. It overcomes the challenges like negtaion, contrast, and context to an extent.

The types of Sentiment Analysis are
* document level sentiment analysis - main polarity of the whole document
* sentence level sentiment analysis - poalrity of the sentence
* entity level   sentiment analysis - polarity of the entities in the sentence


Some of the major challenges in sentiment analysis on news texts are 
* Negation  -   The presence of negation word like "never", "not", "nt", reverses the polarity of the sentences
* Contrast  -   The presence of contrastive conjunctions like "but", "however", "although" conatin sentiment phrases with different                     polarity
* Context   -   The polarity of a sentence changes according to the context and domain
* Comparative - Difficult to predict the polarity of the sentnece with "than". E.g. "Coke is better than Pepsi" , difficult to predict                 the polarity of sentence

## Motivation
The Sentiment Analysis on News Text has  a lot of applications in the real world
* Get the perspective of the author towards an event through document lvel sentiment analysis
* Get the media perspective of politicians in an election period
* Predict the stock price

## TABLE OF CONTENTS

* Input Data
* Create Bigrams and Unigrams from Input data
* Machine learning Algorithms Deployed on Bigrams and Unigrams
* Accuracy Table of different algorithms on different data
* Stanford Classifeir with dictonary methods
* Precision and Recall of Stanford Classifier with dictionary methods on 5w answers


 ### Input Data
500 news articles (3000 sentences) from the Reuters dataset are manually annotated using Gate Tool to build an input dataset for this project. A new annotation schema is developed centered on sentence level and source_phrase level sentiment analysis. This sentence level annotation schema is used to get the overall polarity of the texts, while the source_phrase level annotation is based on the concept of entitx and aspect level sentiment analysis. Target level sentiment comes in handy in situations where there can be sentences in the news texts which contain word phrases describing the sentiment towards a target. These word phrases have entirely different polarity or same polarity as that of the sentences. Entity level sentiment analysis is another approach to make sentiment analysis more efficient.

### Create Bigrams and Unigrams from Input data
The text data is obtained from the Gate Xml files using Java XML Parser and regular expressions. Finally the annotated sentences are obtained from the Gate XML files in the form of files containing the sentences according to their polarity. **Positive_sentence.txt** contains positive annotated sentences and P**ositive_source_phrase_txt** contains positive source phrases. These texts are used for obtaining unigrams and bigrams input to the classifier. The text data from the files is tokenized, stemmed and stored to pass it to the training and test phase of the classifier.

**ReadXMLfile.java** – Parse the XML files and retrieve the annotated sentences from the Gate XML files.

**Create_unigrams.java** – Create unigrams or tokens from the sentences and source_phrases .

**Output**- **training_sentence_tokens.csv** – input file containing tokens from the sentence annotations. It is used to train the classifier. The rows of file with entries {tokens, label} , test_sentence_tokens.csv also contains the same format.
**training_source_phrase_tokens.csv** – input file containing tokens from the source_phrase annotations. It is used to train the classifier. The rows of file with entries {tokens, label}, test_source_phrase.csv also contains the same format, used to test the classifier.

**Create_bigrams.java** - Create bigrams from from the sentences and source_phrases

**Output**- **training_sentence_bigrams.csv** – input file to train the classifier contains rows with entries {bigrams, label} and test_sentence_bigarms.csv also contains the same format.
**training_source_phrase_bigrams.csv** – input file containing bigrams rom the source_phrase annotations. It is used to train the classifier. The rows of file with entries {bigrams, label}, test_source_phrase_bigarms.csv also contains the same format, used to test the classifier.

### Machine Learning Algorithms Deployed
A baseline accuracy is set using the existing machine learning algorithms on the unigrams, bigrams, trigrams derived from sentences in the article. The accuracy obtained using machine learning algorithm of 50%. The major problem with the machine learning algorithms is that it doesn't handle the challenges well such as negation, contrast, semnatic context.

  #### 1)	Naïve Bayes Classifier (WEKA library)
     Naïve Bayes Classifier is implemented using WEKA machine learning library. It is a probabilistic classifier used for predicting the classes based on Bayes Theorem. The features do not have any mutual dependence on predicting the outcome.  Bag of Words dataset is the input with tokens.
         Two types of naïve bayes Classifeir is used in our project
         a)Naïve Bayes Classifier with unigrams as the input data ( NaïveBayesAlgorithm.java)
            The input data in the format {tokens, label} E.g.: {good, positive} is created from the annotated texts. The label is set as the class attribute to be predicted. The training_sentence_tokens.csv, converted into ARFF format in WEKA, is used to train the classifier while the test_sentence_tokens.csv is used to test the classifier.
        b)Naïve Bayes Classifier with bigrams as the input data( NaïveBayesAlgorithm_Bigrams.java)
          The bigrams can sometimes improve the performance of the classifier as the nearby words together as an input can add more meaning to the context which helps in the text classification. The input data in the format {bigrams, Label}. The training_sentence_bigrams.csv, converted into ARFF format in WEKA ,is used to train the classifier while the test_sentence_bigrams.csv is used to test the classifier.

  #### 2)	Support Vector Machines (LIbSVM)
   Support Vector Machines is implemented using the LibSVM machine learning. In order to give the data as input to the LibSVM, it is necessary to convert first into the LIBSVM data format {label word1_index:word1_count word2_index:word2_count} where label is the polarity of the word and word1_index is the index of the first word in the sentence and word_count is the frequency of the word in the whole dataset. To imprve the accuracy, further modifications like scaling of the word count, applying grid serach with different values for the parameters C and gamma.

The input data in the form of sentences in the LibSVM data format. 
E.g.: This is a great book

          The LibSVM data format:  1        1:0.5467     2:0.6758      3:0.7654



Three types
The classifier is categorized into three types based on the input data. If the input LIBSVM data format is constructed with each row encoding the sentence, then it is a general classifier. If the row encoded is a unigram, then SVM Calssifier(Unigrams) and finally bigrams then the SVM classifier(Bigrams).
* SVM classifier (General)(SupportVectorMachines.java)
  The LibSVm data with each row encoding a sentence. The format of the data is elaborated above.
* SVM classifier (Unigrams) (SupportVectorMachines_Unigrams.java)
  The LibSVm data with each row encoding a unigrams . 
*SVM classifier (Bigrams)  (SupportVectorMachines_Bigrams.java)
The LibSVm data with each row encoding a bigrams.


#### 3)Maximum Entropy Classifier
   Maximum Entropy Classifier is a probabilistic classifier. It is based on the concept of the conditional dependencies between the features unlike the Naïve Bayes Classifier in which the features are independent. The model with maximum information entropy is selected during the training phase through a series of iterations, which is later applied to the test dataset, to retrieve the predictions.

      1)	With Unigrams (MaximumEntropyClassifier.java)
          In this classifier, the Unigrams are the input dataset. Unigrams are given as input to Maximum Entropy Classifier with the  dataset in the format {polarity,word}. 

      2)With Bigrams() (MaximumEntropyClassifier_Bigrams.java)
        Bigrams are used in the classifier as the two proximity words can add more meaning to the polarity in certain cases. As some classifiers has shown marginal increase in accuracy due to the usage of the bigrams, bigrams can be used as a dataset in the text classification algorithms.
        
### Accuracy Table

| Algorithm     | Accuracy (Sentences)|
| ------------- | ------------- |
| 1) Naive Bayes Classifier  |   | 
| * Unigrams | 47.045%  |
| * Bigrams | 49.2424%  | 
| 2) Support Vector Machines  |   |   
| * Sentences (General) | 50.72%  | 
| * Unigrams | 49.2693% | 
| * Bigrams | 49.39103%  | 
| 3) Maximum Entropy Classifier  |   |   
| * Unigrams | 49.2693%  | 
| * Bigrams | 49.3910%  | 

### Stanford Classifier with dictionary methods

Use 5w Extractor to extract the 5w answers of the article. Run the stanford classifier on the 5w answers dataset. Compute the main polarity of the article by finding the classes with maximum number of predictions. Check the prediction with the manually annotated main polarity. If it doesn't matches, pass the document to dictionary methods. The 5w answers of wrongly predicted articles is undergone dictionary methods to get the main polarity. Again, its rechecked with the manual annotated main polarity. Compute the precision and recall of each class.

### Precision and Recall of Stanford Classifier with Dictionary methods on 5w answers




<img src="https://user-images.githubusercontent.com/7453418/33634339-1d6baa5e-da14-11e7-9cd1-f208dbe9281b.jpg" width="750" height="500">

## Usage

Delete the all the files in the folder except 5w files nad the data folder

Input 
* data folder containing annotated Gate XML files
* 5w files containing 5w answers of the article

1) Create the unigrams by running the create_tokens.java
2) Create the bigrams and trigrams by running the create_ngrams.java
3) Choose and run the machine learning algorithms on the classifier package
4) Run W_questions.java to get the stanford predictions of w answers 
5) Run the Article_Stanford_Sentiment.java to get the stanford predictions of first five sentences and whole text in the article
5) Run Compare_methods to perform the sentiment analysis using Stanford Classifier with dictionary methods

Word_Senses_Sentence.java - Get the senses of the words according to the context after performing word sense disambiguation

##Installation

Install libsvm, opennlp-maxent-3.0.1-incubating, stanford-corenlp-3.7.0-models-english, weka-stable-3.6.6 libraries, effect lexicon and subjective lexicon in the lexicon folder in resource


         
