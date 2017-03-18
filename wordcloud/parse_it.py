import email
import os
import glob2
import string

path = 'C:\\Users\\Ethan\\Desktop\\gyb\\GYB-GMail-Backup-e.r.nordness@gmail.com\\**\\*.eml'
output = 'dump.txt'

def get_decoded_email_body(msg):
    """ Decode email body.
    Detect character set if the header is not set.
    We try to get text/plain, but if there is not one then fallback to text/html.
    :param message_body: Raw 7-bit message body input e.g. from imaplib. Double encoded in quoted-printable and latin-1
    :return: Message body as unicode string
    """

    text = ""
    if msg.is_multipart():
        html = None
        for part in msg.get_payload():

            if part.get_content_charset() is None:
                #print "%s, %s" % (part.get_content_type(), part.get_content_charset())
                continue

            charset = part.get_content_charset()

            if part.get_content_type() == 'text/plain':
                text = unicode(part.get_payload(decode=True), str(charset), "ignore").encode('utf8', 'replace')

            if part.get_content_type() == 'text/html':
                html = unicode(part.get_payload(decode=True), str(charset), "ignore").encode('utf8', 'replace')

        if text is not None:
            return text.strip()
        else:
            return html.strip()
    else:
        text = unicode(msg.get_payload(decode=True), msg.get_content_charset(), 'ignore').encode('utf8', 'replace')
        return text.strip()

with open(output, 'w') as out:
    for f in glob2.glob(path):
        with open(f) as eml:
            msg = email.message_from_file(eml)
            text = get_decoded_email_body(msg)
            if text:
                #out.write(msg['Date'])
                #out.write(': ')
                text = text.translate(None, string.punctuation)
                out.write(text)
                out.write('\n')
