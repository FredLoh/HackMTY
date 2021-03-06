"""
    The Route Layer.
    Here the message is routed to its proper view.
    The routes are defined with regular expressions and callback functions (just like any web framework).
"""
import threading
import re
import logging
from yowsup.layers.protocol_messages.protocolentities import TextMessageProtocolEntity
from yowsup.layers.interface import YowInterfaceLayer, ProtocolEntityCallback

from views import basic_views

ilyregex = "(Te Amo|te amo|Te amo|I love you|i love you|ily|ILY|Ily|TE AMO|I LOVE YOU|\<3)"
tyregex = "(Thanks|thanks|THANKS|THANK YOU|Thank you|Thank You|thank you|thankyou|Thankyou|THANKYOU|Gracias|gracias|GRACIAS|thx|THX|Thx|TY|Ty|ty)"
gnregex = "(Gute nacht|Good night|good night|Good Night|GOOD NIGHT|BUENAS NOCHES|Buenas noches|buenas noches|Buenas Noches|Hasta ma.ana|hasta ma.ana|Hasta Ma.ana|HASTA MA.ANA)"
hregex = "(Que Onda|que onda|Que onda|Hola|hola|HOLA|HELLO|Hello|hello|Good morning|GOOD MORNING|good morning|buenos d.as|Buenos d.as)"
gbregex = "(Adi.s|adi.s|ADI.S|Good bye|GOODBYE|Good Bye|goodbye|Goodbye|good bye|GOOD BYE)"
bsregex = "(Boto-san|BotoSan|boto-san|Boto-San|Boto San|botosan|boto san|Boto san|Botosan|BOTOSAN|BOTO SAN|BOTO-SAN)"


# Basic regex routes
routes = [
          ("/partido-start-(?P<answer>[^$]+)-(?P<hora>[^$]+)-(?P<duration>[^$]+)", basic_views.start_partido),
          ("#", basic_views.check_disk),
          ("/list", basic_views.list_players),
          ("/clear", basic_views.partido_end),
          ("/end", basic_views.close)
          # ("^/e(cho)?\s(?P<echo_message>[^$]+)$", basic_views.echo),
          # ("^/about", basic_views.about_me),
          # ("^!q", trivia_module.ask_question),
          # ("^!giveup", trivia_module.provide_answer),
          # ("^!a?\s(?P<answer>[^$]+)$", trivia_module.check_answer),
          # ("^/dev", basic_views.dev_plans),
          # ("^/roll", basic_views.roll),
          # ("^/rules", basic_views.rules),
          # ("^/jadensmith", basic_views.jaden),
          # ("^/dolar", basic_views.dollar),
          # ("^/europena", basic_views.euro_pena),
          # ("^/w", basic_views.wisdom),
          # ("^/meaningoflife", basic_views.meaning),
          # ("^/caracolamagica", basic_views.caracola),
          # ("^/lotr", basic_views.lord_of_the_rings),
          # ("(.*" + ilyregex + "(.|\n)*" + bsregex + ".*)|(.*" + bsregex + "(.|\n)*" + ilyregex + ".*)", basic_views.love_you),
          # ("(.*" + tyregex + "(.|\n)*" + bsregex + ".*)|(.*" + bsregex + "(.|\n)*" + tyregex + ".*)", basic_views.thank_you),
          # ("(.*" + gnregex + "(.|\n)*" + bsregex + ".*)|(.*" + bsregex + "(.|\n)*" + gnregex + ".*)", basic_views.good_night),
          # ("(.*" + hregex + "(.|\n)*" + bsregex + ".*)|(.*" + bsregex + "(.|\n)*" + hregex + ".*)", basic_views.hello),
          # ("(.*" + gbregex + "(.|\n)*" + bsregex + ".*)|(.*" + bsregex + "(.|\n)*" + gbregex + ".*)", basic_views.goodbye),
          # ("^/help", basic_views.help),
          # (".*(rana|Rana|Adrian|adrian).*", basic_views.rana)
          ]


class RouteLayer(YowInterfaceLayer):
    def __init__(self):
        self.partido_mode = False
        self.partido_list = []
        """
            The definition of routes and views (callbacks)!

            For the simple message handling, just calls the callback function, and expects a message entity to return.
            For more complex handling, like asynchronous file upload and sending, it creates a object passing 'self',
            so the callback can access the 'self.toLower' method
        """

        super(RouteLayer, self).__init__()

        # Google views to handle tts, search and youtube
        # routes.extend(GoogleViews(self).routes)
        #
        # # Bing views to handle image search
        # routes.extend(BingViews(self).routes)
        #
        # # Media views to handle url print screen and media download
        # routes.extend(MediaViews(self).routes)
        #
        # # adds super fun views
        # routes.extend(SuperViews(self).routes)
        # group admin views disabled by default.
        # read the issue on: https://github.com/joaoricardo000/whatsapp-bot-seed/issues/4
        # enable on your own risk!
        # routes.extend(GroupAdminViews(self).routes)

        self.views = [(re.compile(pattern), callback) for pattern, callback in routes]

    def route(self, message):
        """Get the text from message and tests on every route for a match"""
        text = message.getBody()

        # if self.partido_mode:
        #     if message.getBody()[:1] == "#":
        #         if str(message.getPartipant()) not in self.partido_list:
        #             self.partido_list.append(str(message.getParticipant()))
        #             print self.partido_list

        if self.partido_mode:
            if text[:14] == "/partido-start":
                text = ""


        if text[:14] == "/partido-start":
            self.partido_mode = True

        if not self.partido_mode:
            text = ""

        if self.partido_mode:
            if text == "/end":
                self.partido_mode = False




        if text == "/clear":
            self.partido_mode = False
        # print("Participant: " + message.getParticipant())
        # print(message.getBody())
        #
        # # if message.getBody() == "!q":
        # # elif message.getBody()[:2] == "!a":
        # #     if trivia_module.check_answer(message.getBody()[2:]):
        # # #         print "k"
        # #     else:
        # #         print "kk"
        # #         #incorrect
        #
        # #  Ban Ed
        # if(str(message.getParticipant()) == "5218116618135@s.whatsapp.net"):
        #     text = ""
        # #  Correct beban spelling
        # if(str(message.getParticipant()) == "5218183660872@s.whatsapp.net"):
        #     if(str(message.getBody())[0] != "/"):
        #         if(str(message.getBody())[0] != "h"):
        #             text = "beban"
        # #  Correct ed spelling
        # if(str(message.getParticipant()) == "5218116618135@s.whatsapp.net"):
        #     if(str(message.getBody())[0] != "/"):
        #         if(str(message.getBody())[0] != "h"):
        #             text = "beban"
        #     routes.extend(SuperViews(self).routes)
        #     text = message.getBody()
        for route, callback in self.views:
            match = route.match(text)
            if match:  # in case of regex match, the callback is called, passing the message and the match object
                threading.Thread(target=self.handle_callback, args=(callback, message, match)).start()
                break

    def handle_callback(self, callback, message, match):
        try:
            # log message request
            if (message.isGroupMessage()):
                logging.info("(GROUP)[%s]-[%s]\t%s" % (message.getParticipant(), message.getFrom(), message.getBody()))
            else:
                logging.info("(PVT)[%s]\t%s" % (message.getFrom(), message.getBody()))
            # execute callback request
            data = callback(message, match)
            if data: self.toLower(data)  # if callback returns a message entity, sends it.
        except Exception as e:
            logging.exception("Error routing message: %s\n%s" % (message.getBody(), message))

    @ProtocolEntityCallback("message")
    def on_message(self, message):
        "Executes on every received message"
        self.toLower(message.ack())  # Auto ack
        self.toLower(message.ack(True))  # Auto ack (double blue check symbol)
        # Routing only text type messages, for now ignoring other types. (media, audio, location...)
        if message.getType() == 'text':
            self.route(message)

    @ProtocolEntityCallback("receipt")
    def on_receipt(self, entity):
        "Auto ack for every message receipt confirmation"
        self.toLower(entity.ack())
