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

export default function Post(post: PostData) {
  const sanitizedContent = sanitizeHtml(post.content, {
    allowedTags: [],
    allowedAttributes: {},
  });

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

        <Box size="sm" dangerouslySetInnerHTML={{ __html: sanitizedContent }} />
        <br />
        <Text size="xs" ta="right">
          {post.author}
        </Text>
      </Card>
    </>
  );
}
