import Post from "@/app/components/Post";
import { PostData, SessionData } from "@/app/lib/definitions";
import { fetchPost } from "@/app/lib/fetchPost";
import { getSessionData } from "@/app/lib/sessionUtil";
import { Button } from "@mantine/core";
import link from "next/link";

export default async function getPostById({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const response = await fetchPost(id);
  const post: PostData = response.data;
  const sessionData: SessionData = await getSessionData();

  return (
    <>
      {sessionData.isAuthenticated &&
        sessionData.authorities.includes("ADMIN") && (
          <Button component={link} href={`/posts/${id}/edit`}>
            Edit Post
          </Button>
        )}
      <Post {...post} />
    </>
  );
}
