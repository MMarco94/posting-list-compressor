# Posting list compressor
This project aims to compress a posting list by reassigning the documents id.

To do so, documents are clustered according to their Jaccard similarity.  
Then TSP is computed between clusters, and the new document id is assigned such that element in the same
cluster have consequent id (the order of documents inside a cluster is not taken into account).

By doing so, we expect that the average D-Gap in the posting list will becomes smaller, thus reducing
the number of bits required to store it.

# Results
To measure how good this technique is, the dataset http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/lyrl2004_rcv1v2_README.htm
has been analyzed.  
The size of the posting lists has been measured using different encodings, with and without the id reassigned

| Encoding         | Original size | Size with id reassigned | Saving       |Saving % |
| ---------------- | -------------:|------------------------:| ------------:| ------:|
| VBCode           | 68.51 MB      | 64.90 MB                |  3 MB 628 KB |  5.27% |
| Elias Gamma Code | 62.16 MB      | 47.27 MB                | 14 MB 917 KB | 23.95% |
| Elias Delta Code | 52.71 MB      | 39.48 MB                | 13 MB 229 KB | 25.10% |

As you can see, every encoding has been improved and some space has been saved. Most remarkably, both Elias encoding
saw an improvement of about 25%.  
This is particularly impressive considering that for time and memory constraints, the clustering and TSP algorithms used were
sub-optimal.