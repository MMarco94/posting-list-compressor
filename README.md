# Posting list compressor
This project aims to compress a posting list by reassigning the documents id.

To do so, documents are clustered according to their Jaccard similarity.  
Then TSP is computed between clusters, and the new document id is assigned such that element in the same
cluster have consequent id (the order of documents inside a cluster is not taken into account).

By doing so, we expect that the average D-Gap in the posting list will becomes smaller, thus reducing
the number of bits required to store it.

# Results
On the dataset obtained from http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/lyrl2004_rcv1v2_README.htm, the results were the following:
