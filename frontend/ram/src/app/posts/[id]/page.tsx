import Post from "@/app/components/Post";

export default async function getPostById({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;

  const API_POST_URL = process.env.API_POST_URL;
  const USERNAME = process.env.API_USERNAME;
  const PASSWORD = process.env.API_PASSWORD;

  const data = await fetch(API_POST_URL + "/" + id, {
    headers: {
      Authorization:
        "Basic " + Buffer.from(`${USERNAME}:${PASSWORD}`).toString("base64"),
    },
  });
  const response = await data.json();
  const post = response;
  console.log(">>>" + {...post});
  return <Post {...post}/>
}
