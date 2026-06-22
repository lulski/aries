import Post from "@/app/components/Post/Post";
import { PostData, SessionData } from "@/app/lib/definitions";
import { fetchPostById, fetchPostByTitle } from "@/app/lib/postsApiCall";
import { getSessionData } from "@/app/lib/sessionUtil";
import { Button, Group } from "@mantine/core";
import Link from "next/link";

type PageProps = {
  params: Promise<{
    param: string;
  }>;
};
export default async function viewPost({ params }: PageProps) {
  const { param } = await params;

  const isNumeric = /^\d+$/.test(param);

  let response;
  if (isNumeric) {
    // fetch by numeric ID
    response = await fetchPostById(param);
  } else {
    // fetch by slug
    response = await fetchPostByTitle(param);
  }
  const post: PostData = response;
  const sessionData: SessionData = await getSessionData();

  return (
    <>
      <Group justify="flex-end" mb={"10px"}>
        {sessionData.isAuthenticated &&
          sessionData.authorities.includes("ADMIN") && (
            <Link href={`/posts/${param}/edit`}>
              <Button size="md">Edit Post</Button>
            </Link>
          )}
      </Group>
      <Post {...{ post, allowHtmlMarkup: true }} />
    </>
  );
}
