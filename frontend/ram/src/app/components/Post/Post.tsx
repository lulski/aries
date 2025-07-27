import { Box, Card, CardSection, Group, Text, Title } from "@mantine/core";
import sanitizeHtml from "sanitize-html";
import { PostData } from "../../lib/definitions";

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
