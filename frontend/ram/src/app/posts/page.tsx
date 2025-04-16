import Post from "@/components/Post";

//export default async function Page() {
export default async function posts() {
  const API_POST_URL = process.env.API_POST_URL;
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;

  const data = await fetch(API_POST_URL, {
    headers: {
      Authorization:
        "Basic " + Buffer.from(`${USERNAME}:${PASSWORD}`).toString("base64"),
    },
  });
  console.log(data);
  const response = await data.json();
  const posts = response.postDto;

  return (
    <div>
      <h1>List of posts</h1>
      <ul>
        {posts.map((post, index) => (
          <li key={index}>
            <Post {...post}></Post>
          </li>
        ))}
      </ul>
    </div>
  );
}
