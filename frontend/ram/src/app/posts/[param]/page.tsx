import Post from "@/app/components/Post";
import { PostData, SessionData } from "@/app/lib/definitions";
import { fetchPostById, fetchPostByTitle } from "@/app/lib/fetchPost";
import { getSessionData } from "@/app/lib/sessionUtil";
import { Button, Group } from "@mantine/core";
import link from "next/link";

type Params = {
  param: string;
};

export default async function viewPost({ params }: { params: Params }) {
  const { param } = params;

  const isNumeric = /^\d+$/.test(param);

  let response;
  if (isNumeric) {
    // fetch by numeric ID
    response = await fetchPostById(param);
  } else {
    // fetch by slug
    response = await fetchPostByTitle(param);
  }
  const post: PostData = response.data;
  const sessionData: SessionData = await getSessionData();

  return (
    <>
      <Group justify="flex-end" mb={"10px"}>
        {sessionData.isAuthenticated &&
          sessionData.authorities.includes("ADMIN") && (
            <Button component={link} href={`/posts/${param}/edit`}>
              Edit Post
            </Button>
          )}
      </Group>
      <Post {...post} />
    </>
  );
}
