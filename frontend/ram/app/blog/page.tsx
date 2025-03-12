//import { getPosts } from '@/lib/posts'
//import { Post } from '@/ui/post'
 
//export default async function Page() {
export default async function whatever() {

    const API_URL = process.env.API_POST_URL;
    const USERNAME = process.env.API_USERNAME;
    const PASSWORD = process.env.API_PASSWORD;

    const data = await fetch(API_URL, {headers:{
        'Authorization': 'Basic ' + Buffer.from(`${USERNAME}:${PASSWORD}`).toString('base64'),
    }})
    console.log(data);
    const response = await data.json()
    const posts = response.postDto;

 
  return (
    <div>
    <h1>List of posts</h1>
    <ul>
      {posts.map((post, index) => (
        <li key={index}>
          <h3>{post.title}</h3>
          <p>{post.content}</p>
          <p><strong>Author:</strong> {post.author}</p>
        </li>
      ))}
    </ul>
  </div>
  )
}