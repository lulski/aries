import Post from "@/components/Post";
import { Button, Container, Group } from "@mantine/core";
import { getSessionData } from "../lib/sessionUtil";
import PostInline from "../components/PostInline";

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

  const sessionData = await getSessionData();
  console.log("sessionData: " + sessionData);

  return (
    <>
      {sessionData.isAuthenticated &&
        sessionData.authorities.includes("ADMIN") && (
          <Container>
            <Group justify="flex-end">
              <Button
                component="a"
                href="/posts/new"
                bottom={"10px"}
                pos={"relative"}
              >
                Post New
              </Button>
            </Group>
          </Container>
        )}
      <Container>
        <ul>
          {posts.map((post, index) => (
            <li key={index}>
              <PostInline {...post}></PostInline>
            </li>
          ))}
        </ul>
      </Container>
    </>
  );
}
