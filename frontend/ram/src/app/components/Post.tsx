import {
  Card,
  Image,
  Box,
  Text,
  Group,
  Title,
  CardSection,
  Divider,
  NavLink,
} from "@mantine/core";
import { PostData } from "../lib/definitions";
import sanitizeHtml from "sanitize-html";
import Link from "next/link";

type PostProps = {
  post: PostData;
  allowHtmlMarkup: boolean;
};

export default function Post({ post, allowHtmlMarkup }: PostProps) {
  let displayContent;

  const sanitizedContent = sanitizeHtml(post.content, {
    allowedTags: [],
    allowedAttributes: {
      a: ["href", "target", "rel"],
    },
  });
  console.log("allowHtmlMarkup: " + allowHtmlMarkup);
  if (allowHtmlMarkup) {
    displayContent = post.content;
  } else {
    displayContent = sanitizedContent;
  }

  return (
    <>
      <Card
        shadow="sm"
        padding="lg"
        radius="md"
        withBorder
        component={Link}
        href={`/posts/${post.title}`}
        styles={{ root: { marginBottom: 10 } }}
        id={post.id}
      >
        <CardSection>
          {/* <Image src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-10.png" /> */}
        </CardSection>

        <Group justify="space-between" mt="md" mb="xs">
          <Title order={5}>{post.title}</Title>
        </Group>

        <Box size="sm" dangerouslySetInnerHTML={{ __html: displayContent }} />
        <br />
        <Text size="xs" ta="right">
          {post.author}
        </Text>
        <Text size="xs" ta="right">
          {post.createdOn}
        </Text>
      </Card>
    </>
  );
}
