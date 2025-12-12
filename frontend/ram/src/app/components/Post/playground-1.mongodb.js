/* global use, db */
// MongoDB Playground
// Use Ctrl+Space inside a snippet or a string literal to trigger completions.

// The current database to use.
use("aries_db");

// Search for documents in the current collection.
db.getCollection("posts")
  .find(
    {
      /*
      * Filter
       fieldA: value or expression
      */
      title: { $regex: "mge", $options: "i" }, // matches '%project%' case-insensitive
    },
    {
      /*
       * Projection
       * _id: 0, // exclude _id
       * fieldA: 1 // include field
       */
    }
  )
  .sort({
    /*
     * fieldA: 1 // ascending
     * fieldB: -1 // descending
     */
  });
