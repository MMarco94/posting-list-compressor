# Postings list compressor
This project aims to compress a postings list by reassigning the documents id.

To do so, documents are clustered according to their Jaccard similarity.  
Then TSP is computed between clusters, and the new document id is assigned such that element in the same
cluster have consequent id (the order of documents inside a cluster is not taken into account).

By doing so, we expect that the average D-Gap in the postings list will becomes smaller, thus reducing
the number of bits required to store it.

# Implementation details
To achieve this it is necessary to solve a clustering problem between a large number of documents, and a TSP between a potentially large
number of clusters. Some compromises have been made to allow the code to run on a normal desktop computer in a reasonable amount of time.  
In particular:
- The Christofides algorithm for solving the TSP has been used. It's a 1.5 approximation algorithm that works on a metric space (the Jaccard similarity is metric)
- The number of clusters has been limited to 2500, to limit the size of the graph for the TSP
- A simple stream clustering algorithm has been used to divide the documents in clusters

Those limitations could be solved by using other algorithm implementations, but that's beside the purpose of this project. 

# Results
To measure how good this technique is, the dataset available at http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/lyrl2004_rcv1v2_README.htm
has been analyzed.  
The size of the postings list has been measured using different encodings, with and without the id reassigned.

| Encoding         | Original size | Size with id reassigned | Saving       |Saving % |
| ---------------- | -------------:|------------------------:| ------------:| ------:|
| VBCode           | 68.51 MB      | 64.90 MB                |  3 MB 638 KB |  5.31% |
| Elias Gamma Code | 63.20 MB      | 47.01 MB                | 14 MB 13 KB | 22.17% |
| Elias Delta Code | 54.51 MB      | 42.44 MB                | 12 MB 72 KB | 22.14% |

As you can see, every encoding has been improved and some space has been saved. Most remarkably, both Elias encodings
saw an improvement of about 22%. 
This is particularly impressive considering that for time and memory constraints, the clustering and TSP algorithms used were
sub-optimal.
