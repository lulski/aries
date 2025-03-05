//import { getPosts } from '@/lib/posts'
//import { Post } from '@/ui/post'
 
//export default async function Page() {
export default async function whatever() {

    const API_URL = process.env.API_POST_URL;
    const USERNAME = process.env.API_USERNAME;
    const PASSWORD = process.env.API_PASSWORD;



    const data = await fetch(API_URL)
    const posts = await data.json()

 // const posts = await getPosts()
 
  return (
    <div>
    <h1>List of post</h1>
    <ul>
      {
      posts.map((post) => (<li key={post.id}>{post.title}</li>))
      }
    </ul>
    </div>
 
  )
}